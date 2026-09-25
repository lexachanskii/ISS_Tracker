package com.alex.controller;


import com.alex.data.DTO.DataDto;
import com.alex.data.common.ApiConfig;
import com.alex.data.common.ObservationVerdict;
import com.alex.logic.FileValidator;
import com.alex.logic.ObservationAnalyzer;
import com.alex.logic.ParamsSetter;
import com.alex.parser.JsonParser;
import com.alex.service.Agregation;
import com.alex.writer.CSVWriter;
import com.alex.writer.JsonWriter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Application {
    public void run(ApiConfig config, String format, String filename) throws IOException {
        Agregation agregator = new Agregation(config.apis());
        List<DataDto> data = agregator.collectALL();
        ObservationAnalyzer analyzer = new ObservationAnalyzer();
        ObservationVerdict verdict = analyzer.getObservationVerdict(data);

        writeVerdict(verdict, format, filename, false);

    }

    public void runInteractive(ApiConfig config, String format, String outputFile) throws IOException {
        Scanner scanner = new Scanner(System.in);

        boolean append = false;
        ObservationVerdict lastVerdict = null;
        List<DataDto> currentData = null;
        HashMap<String, Map<String, String>> customParams = new HashMap<>();
        HashMap<String, List<String>> customPaths = new HashMap<>();


        while (true) {
            printBoard(format, append);
            switch (scanner.nextLine().trim()) {
                case "1" -> {
                    System.out.println("Введите api для настройки параметра из списка ниже: ");
                    showNames(config);

                    String api = scanner.nextLine().trim();
                    switch (api) {
                        case "n2yo"          -> customPaths.put("n2yo", ParamsSetter.setN2YOPath(config.apis().get(0), scanner));
                        case "openweathermap"-> customParams.put("openweathermap", ParamsSetter.setWeatherParams(config.apis().get(1), scanner));
                        case "sunrise-sunset"-> customParams.put("sunrise-sunset", ParamsSetter.setSunriseParams(config.apis().get(2), scanner));
                        default  -> System.out.println("Неизвестный API");
                    }
                    config = ParamsSetter.mergeConfig(config, customPaths, customParams);

                }
                case "2" -> {
                    Agregation agregator = new Agregation(config.apis());
                    currentData = agregator.collectALL();
                }
                case "3" -> {

                    if (currentData == null) {
                        System.out.println("Сначала запусти опрос (2)");
                        break;
                    }

                    System.out.println(JsonParser.MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(currentData));
                }
                case "4" -> {

                    if (currentData == null) {
                        System.out.println("Сначала запусти опрос (2)");
                        break;
                    }

                    ObservationAnalyzer analyzer = new ObservationAnalyzer();
                    lastVerdict = analyzer.getObservationVerdict(currentData);

                    writeVerdict(lastVerdict, format, outputFile, append);
                }
                case "5" -> {

                    if (lastVerdict == null) {
                        System.out.println("Сначала сформируй вердикт (4)");
                        break;
                    }

                    System.out.println(JsonParser.MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(lastVerdict));
                }
                case "6" -> {
                    System.out.print("Формат (json/csv): ");
                    String input = scanner.nextLine().trim();
                    if (input.equals("json") || input.equals("csv")) {
                        format = input;
                        System.out.println("Формат установлен: " + format);
                    } else {
                        System.out.println("Неизвестный формат");
                    }

                    System.out.print("задайте выходной файл: ");
                    String fileInput = scanner.nextLine().trim();

                    FileValidator.validate(fileInput, format);

                    outputFile = fileInput;
                    System.out.println("Файл установлен: " + outputFile);
                }
                case "7" -> {
                    System.out.print("Режим (new/append): ");
                    String input = scanner.nextLine().trim();
                    append = input.equals("append");
                    System.out.println("Режим: " + (append ? "дозапись" : "новый файл"));
                }
                case "8" -> {return;}
                default  -> System.out.println("Неизвестная команда");
            }
        }
    }

    private void writeVerdict(ObservationVerdict verdict, String format, String outputFile, boolean append) throws IOException {
        FileValidator.validate(outputFile, format);

        switch (format) {
            case "json" -> JsonWriter.write(outputFile, verdict, append);
            case "csv"  -> CSVWriter.write(outputFile, verdict, append);
            default     -> throw new RuntimeException("This format is not supported: " + format);
        }
    }

    private void showNames(ApiConfig config) {
        for (ApiConfig.ApiDefinition ent : config.apis()) {
            System.out.println(ent.name());
        }
    }

    private void printBoard(String format, Boolean append) {
        System.out.println("\n=== МКС Трекер ===");
        System.out.println("1. Настроить параметры запроса");
        System.out.println("2. Опросить API");
        System.out.println("3. Показать сырые данные");
        System.out.println("4. Рассчитать вердикт и записать в файл");
        System.out.println("5. Показать последний вердикт");
        System.out.println("6. Формат файла (сейчас:" + format + ")");
        System.out.println("7. Режим записи (сейчас:" + append + ")");
        System.out.println("8. Выход");
        System.out.print("> ");
    }


}

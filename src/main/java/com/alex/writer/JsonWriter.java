package com.alex.writer;

import com.alex.data.common.ObservationVerdict;
import com.alex.parser.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonWriter {

    public static void write(String filename, ObservationVerdict verdict, boolean append) throws IOException {
        File file = new File(filename);

        List<ObservationVerdict> verdicts = new ArrayList<>();

        if (append && file.exists() && (file.length() != 0 ) ) {
            verdicts = new ArrayList<>(Arrays.asList(
                    JsonParser.MAPPER.readValue(file, ObservationVerdict[].class)
            ));
        }

        verdicts.add(verdict);

        try (java.io.FileWriter fw = new java.io.FileWriter(file, false)) {
            JsonParser.MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
            JsonParser.MAPPER.writeValue(fw, verdicts);
        }

        //System.out.println("Written to file: " + file.getAbsolutePath());
    }
}

package com.alex.writer;

import com.alex.data.common.ObservationVerdict;
import com.alex.parser.JsonParser;
import com.github.wnameless.json.flattener.JsonFlattener;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class CSVWriter {
    public static void write(String filename, ObservationVerdict verdict, boolean append) throws IOException {
        File file = new File(filename);
        boolean writeHeader = !append || !file.exists();

        String json = JsonParser.MAPPER.writeValueAsString(verdict);
        Map<String, Object> flat = JsonFlattener.flattenAsMap(json);

        try (java.io.FileWriter fw = new java.io.FileWriter(file, append)) {
            if (writeHeader) {
                fw.write(String.join(",", flat.keySet()) + "\n");
            }
            fw.write(flat.values().stream()
                    .map(v -> v == null ? "" : v.toString())
                    .collect(Collectors.joining(",")) + "\n");
        }

        //System.out.println("Written to file: " + file.getAbsolutePath());
    }
}

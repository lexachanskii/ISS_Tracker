package com.alex.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JsonParser {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> T deserializeDTO(JsonNode node, Class<T> clszz) throws IOException {
        return MAPPER.treeToValue(node, clszz);
    }

    public static <T> T deserialize(String resourcePath, Class<T> clazz) throws IOException {

        try (InputStream is = JsonParser.class
                .getClassLoader()
                .getResourceAsStream(resourcePath)) {

            if (is == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }

            return MAPPER.readValue(is, clazz);
        }
    }
}

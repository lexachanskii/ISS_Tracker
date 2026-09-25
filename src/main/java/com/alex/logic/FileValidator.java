package com.alex.logic;

import java.io.File;

public class FileValidator {

    public static void validate(String filename, String format) {
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("Имя файла не может быть пустым");
        }

        if (!filename.endsWith("." + format)) {
            throw new IllegalArgumentException("Расширение файла должно быть ." + format);
        }

        File f = new File(filename);
        if (f.getParentFile() != null && !f.getParentFile().exists()) {
            throw new IllegalArgumentException("Директория не существует: " + f.getParent());
        }
    }
}
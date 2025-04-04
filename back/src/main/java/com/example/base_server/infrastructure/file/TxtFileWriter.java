package com.example.base_server.infrastructure.file;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class TxtFileWriter {
    public static void writeToFile(String fileName, String content) {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("Invalid filepath.");
        }

        try {
            Files.createDirectories(Paths.get("logs"));
            Path path = Paths.get("logs", fileName);

            Files.write(
                    path,
                    content.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            throw new UncheckedIOException("Fail to create/write file.", e);
        }
    }
}

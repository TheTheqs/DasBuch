package com.example.base_server.utils;

import com.example.base_server.exception.FileStorageException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;

@Component
public class TxtFileUtil {
    private static final String BASE_DIRECTORY = System.getProperty("user.dir") + "/data/";

    //Assure the file exists
    static {
        Path path = Paths.get(BASE_DIRECTORY);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new FileStorageException("Failed to create directory: " + BASE_DIRECTORY, e);
            }
        }
    }
    //Write
    public static void write(String filePath, boolean overwrite, String content) {
        File file = new File(BASE_DIRECTORY + filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, !overwrite))) {
            if (!overwrite) writer.newLine();
            writer.write(content);
        } catch (IOException e) {
            throw new FileStorageException("Error writing to file: " + filePath, e);
        }
    }
    //Read
    public static String read(String filePath) {
        File file = new File(BASE_DIRECTORY + filePath);
        if (!file.exists()) return ""; // Returns empty string if file don't exist.

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new FileStorageException("Error reading file: " + filePath, e);
        }
        return content.toString().trim();
    }
}

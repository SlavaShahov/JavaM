package com.example.utils;

import java.io.*;

public class FileUtils {
    public static void writeToFile(String filename, String content, boolean append) throws IOException {
        System.out.println("Writing to file: " + filename + " Content: " + content.trim());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(content);
            //writer.newLine();
        }
    }

}
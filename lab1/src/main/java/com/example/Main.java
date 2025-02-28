package com.example;

import com.example.utils.FileUtils;
import com.example.FileFilter;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            FileFilter fileFilter = new FileFilter(args);
            fileFilter.processFiles();
            fileFilter.printStatistics();
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
        }
    }
}

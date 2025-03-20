package com.example;

import com.example.utils.FileUtils;
import java.io.*;
import java.util.*;

public class FileFilter {
    private final List<String> inputFiles = new ArrayList<>();
    private String outputPath = "";
    private String prefix = "";
    private boolean appendMode = false;
    private boolean fullStatistics = false;

    private int integerCount = 0;
    private int floatCount = 0;
    private int stringCount = 0;
    private long integerSum = 0;
    private double floatSum = 0.0;
    private int minInteger = Integer.MAX_VALUE;
    private int maxInteger = Integer.MIN_VALUE;
    private double minFloat = Double.MAX_VALUE;
    private double maxFloat = Double.MIN_VALUE;
    private int minStringLength = Integer.MAX_VALUE;
    private int maxStringLength = 0;

    public FileFilter(String[] args) {
        parseArguments(args);
    }

    private void parseArguments(String[] args) {
        System.out.println("Parsing arguments: " + Arrays.toString(args)); // Вывод аргументов
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-o":
                    outputPath = args[++i];
                    break;
                case "-p":
                    prefix = args[++i];
                    break;
                case "-a":
                    appendMode = true;
                    break;
                case "-s":
                    fullStatistics = false;
                    break;
                case "-f":
                    fullStatistics = true;
                    break;
                default:
                    System.out.println("Adding input file: " + args[i]); // Логируем файлы
                    inputFiles.add(args[i]);
                    break;
            }
        }
    }


    public void processFiles() throws IOException {
        for (String inputFile : inputFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    processLine(line);
                }
            }
        }
        //writeResults();
    }

    private void processLine(String line) throws IOException {
        try {
            int integerValue = Integer.parseInt(line);
            integerCount++;
            integerSum += integerValue;
            minInteger = Math.min(minInteger, integerValue);
            maxInteger = Math.max(maxInteger, integerValue);
            FileUtils.writeToFile(outputPath + prefix + "integers.txt", line + "\n", appendMode);
        } catch (NumberFormatException e1) {
            try {
                double floatValue = Double.parseDouble(line);
                floatCount++;
                floatSum += floatValue;
                minFloat = Math.min(minFloat, floatValue);
                maxFloat = Math.max(maxFloat, floatValue);
                FileUtils.writeToFile(outputPath + prefix + "floats.txt", line + "\n", appendMode);
            } catch (NumberFormatException e2) {
                stringCount++;
                minStringLength = Math.min(minStringLength, line.length());
                maxStringLength = Math.max(maxStringLength, line.length());
                FileUtils.writeToFile(outputPath + prefix + "strings.txt", line + "\n", appendMode);
            }
        }
    }

//    private void writeResults() throws IOException {
//        if (integerCount > 0) {
//            FileUtils.writeToFile(outputPath + prefix + "integers.txt", "", appendMode);
//        }
//        if (floatCount > 0) {
//            FileUtils.writeToFile(outputPath + prefix + "floats.txt", "", appendMode);
//        }
//        if (stringCount > 0) {
//            FileUtils.writeToFile(outputPath + prefix + "strings.txt", "", appendMode);
//        }
//    }

    public void printStatistics() {
        System.out.println("Statistics:");
        System.out.println("Integers: " + integerCount);
        if (fullStatistics && integerCount > 0) {
            System.out.println("  Min: " + minInteger);
            System.out.println("  Max: " + maxInteger);
            System.out.println("  Sum: " + integerSum);
            System.out.println("  Average: " + (integerSum / (double) integerCount));
        }
        System.out.println("Floats: " + floatCount);
        if (fullStatistics && floatCount > 0) {
            System.out.println("  Min: " + minFloat);
            System.out.println("  Max: " + maxFloat);
            System.out.println("  Sum: " + floatSum);
            System.out.println("  Average: " + (floatSum / floatCount));
        }
        System.out.println("Strings: " + stringCount);
        if (fullStatistics && stringCount > 0) {
            System.out.println("  Min Length: " + minStringLength);
            System.out.println("  Max Length: " + maxStringLength);
        }
    }

    public void testProcess(String line) throws IOException {
        processLine(line);
    }

    public int getIntegerCount() { return integerCount; }
    public int getFloatCount() { return floatCount; }
    public int getStringCount() { return stringCount; }
    public long getIntegerSum() { return integerSum; }
    public double getFloatSum() { return floatSum; }
    public int getMinInteger() { return minInteger; }
    public int getMaxInteger() { return maxInteger; }
    public double getMinFloat() { return minFloat; }
    public double getMaxFloat() { return maxFloat; }
    public int getMinStringLength() { return minStringLength; }
    public int getMaxStringLength() { return maxStringLength; }


}


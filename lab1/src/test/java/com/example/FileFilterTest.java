package com.example;

import com.example.utils.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class FileFilterTest {

    @TempDir
    Path tempDir;
    // Временная директория для тестов

    @Test
    public void testFileFilterProcessing() throws IOException {
        // Создаем тестовые файлы
        File testFile1 = createTestFile("test1.txt", "123\nabc\n45.67\n");
        File testFile2 = createTestFile("test2.txt", "890\nxyz\n12.34\n");

        // Запускаем утилиту
        String outputPath = tempDir.resolve("output").toString() + "/";
        String[] args = {"-o", outputPath, "-p", "result_", "-f", testFile1.getPath(), testFile2.getPath()};
        FileFilter fileFilter = new FileFilter(args);
        File outputDir = new File(outputPath);
        if (!outputDir.exists()) {
            boolean created = outputDir.mkdirs();
            System.out.println("Created output directory: " + created + ", path: " + outputPath);
        }

//        System.out.println("Output directory path: " + outputPath);
//        System.out.println("Directory exists: " + new File(outputPath).exists());
//        System.out.println("Files in output: " + Arrays.toString(new File(outputPath).listFiles()));

        fileFilter.processFiles();

        // Проверяем результаты
        assertFileContent(outputPath + "result_integers.txt", "123\n890\n");
        assertFileContent(outputPath + "result_floats.txt", "45.67\n12.34\n");
        assertFileContent(outputPath + "result_strings.txt", "abc\nxyz\n");
    }

    @Test
    public void testAppendMode() throws IOException {
        // Создаем тестовые файлы
        File testFile1 = createTestFile("test1.txt", "100\napple\n3.14\n");
        File testFile2 = createTestFile("test2.txt", "200\nbanana\n2.71\n");

        // Запускаем утилиту в режиме добавления
        String outputPath = tempDir.resolve("output").toString() + "/";
        String[] args = {"-o", outputPath, "-p", "result_", "-a", "-f", testFile1.getPath()};
        FileFilter fileFilter1 = new FileFilter(args);

        File outputDir = new File(outputPath);
        if (!outputDir.exists()) {
            boolean created = outputDir.mkdirs();
            System.out.println("Created output directory: " + created + ", path: " + outputPath);
        }

        System.out.println("Output directory path: " + outputPath);
        System.out.println("Directory exists: " + new File(outputPath).exists());
        System.out.println("Files in output: " + Arrays.toString(new File(outputPath).listFiles()));

        fileFilter1.processFiles();

        String[] args2 = {"-o", outputPath, "-p", "result_", "-a", "-f", testFile2.getPath()};
        FileFilter fileFilter2 = new FileFilter(args2);


        fileFilter2.processFiles();

        // Проверяем, что файлы дополнились, а не перезаписались
        assertFileContent(outputPath + "result_integers.txt", "100\n200\n");
        assertFileContent(outputPath + "result_floats.txt", "3.14\n2.71\n");
        assertFileContent(outputPath + "result_strings.txt", "apple\nbanana\n");
    }

    @Test
    public void testStatisticsCalculation() throws IOException {
        String[] args = {"-f"};
        FileFilter fileFilter = new FileFilter(args);

        // Эмулируем обработку строк
        fileFilter.testProcess("100");
        fileFilter.testProcess("200");
        fileFilter.testProcess("3.14");
        fileFilter.testProcess("apple");
        // Проверяем рассчитанные значения
        // Проверяем количество элементов
        assertEquals(2, fileFilter.getIntegerCount(), "Количество целых чисел не совпадает");
        assertEquals(1, fileFilter.getFloatCount(), "Количество вещественных чисел не совпадает");
        assertEquals(1, fileFilter.getStringCount(), "Количество строк не совпадает");

        // Проверяем суммы
        assertEquals(300, fileFilter.getIntegerSum(), "Сумма целых чисел не совпадает");
        assertEquals(3.14, fileFilter.getFloatSum(), 0.001, "Сумма вещественных чисел не совпадает");

        // Проверяем минимальные и максимальные значения
        assertEquals(100, fileFilter.getMinInteger(), "Минимальное целое число не совпадает");
        assertEquals(200, fileFilter.getMaxInteger(), "Максимальное целое число не совпадает");
        assertEquals(3.14, fileFilter.getMinFloat(), 0.001, "Минимальное вещественное число не совпадает");
        assertEquals(3.14, fileFilter.getMaxFloat(), 0.001, "Максимальное вещественное число не совпадает");
        assertEquals(5, fileFilter.getMinStringLength(), "Минимальная длина строки не совпадает");
        assertEquals(5, fileFilter.getMaxStringLength(), "Максимальная длина строки не совпадает");
    }

    @Test
    public void testInvalidInputFile() {
        String[] args = {"-f", "non_existent_file.txt"};
        assertThrows(IOException.class, () -> {
            FileFilter fileFilter = new FileFilter(args);
            fileFilter.processFiles();
        });
    }

    private File createTestFile(String filename, String content) throws IOException {
        File file = tempDir.resolve(filename).toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        }
        return file;
    }

    private void assertFileContent(String filename, String expectedContent) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            assertEquals(expectedContent, content.toString());
        }
    }
}

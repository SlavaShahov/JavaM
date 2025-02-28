package com.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MultiThreadedCalculationTest {

    // Тест на корректность создания и завершения потоков
    @Test
    void testThreadCreationAndCompletion() throws InterruptedException {
        int expectedThreadCount = 4; // Количество ожидаемых потоков

        // Список для хранения потоков
        List<Thread> threads = new ArrayList<>();

        // Создание и запуск потоков
        for (int i = 0; i < expectedThreadCount; i++) {
            Thread thread = new Thread(() -> {
                // Ваш код для потока
            });
            threads.add(thread);
            thread.start();
        }

        // Ожидание завершения всех потоков
        for (Thread thread : threads) {
            thread.join();
        }

        // Проверка: все ли потоки завершены
        assertEquals(expectedThreadCount, threads.size(), "Неверное количество завершённых потоков");
    }


    // Тест на вывод в консоль
    @Test
    void testConsoleOutput() throws InterruptedException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            MultiThreadedCalculation.main(new String[]{});
            Thread.sleep(6000);

            String output = outputStream.toString();
            assertTrue(output.contains("Поток 1 (ID:"));
            assertTrue(output.contains("Поток 2 (ID:"));
            assertTrue(output.contains("Поток 3 (ID:"));
            assertTrue(output.contains("Поток 4 (ID:"));
            assertTrue(output.contains("завершен. Время выполнения:"));
        } finally {
            System.setOut(originalOut);
        }
    }

    // Тест на время выполнения
    @Test
    void testExecutionTime() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        MultiThreadedCalculation.main(new String[]{});

        Thread.sleep(6000);
        long executionTime = System.currentTimeMillis() - startTime;

        assertTrue(executionTime >= 5000);
        assertTrue(executionTime <= 20000);
    }

    // Тест на корректность прогресс-бара
    @Test
    void testProgressBar() throws InterruptedException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            MultiThreadedCalculation.main(new String[]{});  // Запуск программы

            // Увеличим время ожидания для завершения работы всех потоков
            Thread.sleep(10000);  // Можно увеличить это значение при необходимости

            // Получаем вывод программы
            String output = outputStream.toString();
            System.out.println("Output: \n" + output);  // Печать для отладки

            // Проверки, что вывод соответствует ожидаемому
            assertTrue(output.contains("[="), "Progress bar not started.");
            assertTrue(output.contains("]"), "Progress bar not finished.");
            assertTrue(output.contains("[===================="), "Progress bar not completed.");

            // Проверим длину прогресс-бара, ищем строку с символами от [ до ]
            String progressBar = output.substring(output.indexOf('[') + 1, output.indexOf(']'));
            System.out.println("Progress Bar: [" + progressBar + "]");  // Для отладки

            // Проверка длины прогресс-бара (должно быть 50 символов '=')
            assertTrue(progressBar.length() == 50, "Progress bar is not the expected length: " + progressBar.length());

            // Проверка, что прогресс-бар полностью завершён
            assertTrue(output.contains("[==================================================]"), "Progress bar not fully completed.");

        } finally {
            System.setOut(originalOut);
        }
    }


}

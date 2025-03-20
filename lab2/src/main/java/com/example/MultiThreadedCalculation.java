package com.example;

public class MultiThreadedCalculation {

    // Количество потоков
    private static final int THREAD_COUNT = 4;
    // Длина расчёта (количество итераций для прогресс-бара)
    private static final int CALCULATION_LENGTH = 50;
    // Время задержки между итерациями (в миллисекундах)
    private static final int DELAY = 100;

    public static void main(String[] args) {
        // Массив для хранения ссылок на потоки
        Thread[] threads = new Thread[THREAD_COUNT];

        // Создаем и запускаем потоки
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadNumber = i + 1;
            threads[i] = new Thread(new CalculationTask(threadNumber, CALCULATION_LENGTH, DELAY));
            threads[i].start();
        }

        // Ожидаем завершения всех потоков
        for (int i = 0; i < THREAD_COUNT; i++) {
            try {
                threads[i].join(); // Ожидаем завершения каждого потока
            } catch (InterruptedException e) {
                System.out.println("Ожидание завершения потока было прервано.");
                Thread.currentThread().interrupt();
            }
        }

        // Задержка для удаления всех завершённых потоков из списка активных потоков
        try {
            Thread.sleep(100);  // Даем немного времени, чтобы система освободила ресурсы
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Все потоки завершены.");
    }

    // Класс, реализующий задачу для каждого потока
    static class CalculationTask implements Runnable {
        private final int threadNumber;
        private final int calculationLength;
        private final int delay;

        public CalculationTask(int threadNumber, int calculationLength, int delay) {
            this.threadNumber = threadNumber;
            this.calculationLength = calculationLength;
            this.delay = delay;
        }

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            for (int i = 0; i <= calculationLength; i++) {
                // Выводим прогресс-бар
                printProgress(threadNumber, Thread.currentThread().getId(), i, calculationLength);
                try {
                    // Имитация расчёта
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Восстановление флага прерывания
                    return; // Завершаем выполнение потока
                }
            }
            long endTime = System.currentTimeMillis();
            System.out.printf("Поток %d (ID: %d) завершен. Время выполнения: %d мс%n",
                    threadNumber, Thread.currentThread().getId(), endTime - startTime);
        }

        // Метод для вывода прогресс-бара
        private void printProgress(int threadNumber, long threadId, int progress, int total) {
            int barLength = 50;
            int filledLength = (int) ((double) progress / total * barLength);
            StringBuilder bar = new StringBuilder();
            bar.append("[");

            for (int i = 0; i < barLength; i++) {
                if (i < filledLength) {
                    bar.append("=");
                } else {
                    bar.append(" ");
                }
            }

            bar.append("]");
            System.out.printf("Поток %d (ID: %d) %s %d%%%n", threadNumber, threadId, bar.toString(), (int) ((double) progress / total * 100));
        }
    }

    public static int getCalculationLength() {
        return CALCULATION_LENGTH;
    }

    public static int getDelay() {
        return DELAY;
    }
}

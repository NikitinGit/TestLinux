package com.example.testlinux.service.thread;

public class ThreadUncouthException {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            // Имитация ошибки, без обработки try-catch
            throw new RuntimeException("Необработанная ошибка в потоке");
        });

        // Устанавливаем обработчик необработанных исключений
        thread.setUncaughtExceptionHandler((t, e) -> {
            System.out.println("Поток " + t.getName() + " завершился с исключением: " + e.getMessage());
        });

        thread.start();
    }
}

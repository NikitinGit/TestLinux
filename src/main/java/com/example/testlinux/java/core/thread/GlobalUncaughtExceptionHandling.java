package com.example.testlinux.java.core.thread;

public class GlobalUncaughtExceptionHandling {
    public static void main(String[] args) {
        // Устанавливаем глобальный обработчик необработанных исключений
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.out.println("Поток " + t.getName() + " завершился с исключением: " + e.getMessage());
        });

        Thread thread = new Thread(() -> {
            // Имитация ошибки без обработки try-catch
            throw new RuntimeException("Необработанная ошибка в потоке 25");
        });

        MyThread1 thread1 = new MyThread1();
        new Thread(thread1, "Thread name; ").start();
        thread.start();
    }
}

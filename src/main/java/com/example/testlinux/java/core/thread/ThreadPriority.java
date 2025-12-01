package com.example.testlinux.java.core.thread;

public class ThreadPriority {
    public static void main(String[] args) {
        Thread lowPriorityThread = new Thread(() -> {
            System.out.println("Низкоприоритетный поток начал работу.");
        });

        Thread highPriorityThread = new Thread(() -> {
            System.out.println("Высокоприоритетный поток начал работу.");
        });

        lowPriorityThread.setPriority(Thread.MIN_PRIORITY); // Приоритет 1
        highPriorityThread.setPriority(Thread.MAX_PRIORITY); // Приоритет 10

        lowPriorityThread.start();
        highPriorityThread.start();
    }
}

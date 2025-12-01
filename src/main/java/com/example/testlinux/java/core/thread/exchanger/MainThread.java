package com.example.testlinux.java.core.thread.exchanger;

import java.util.concurrent.Exchanger;

public class MainThread {

    public static void main(String[] args) {

        int count = 0;
        Exchanger<Integer> ex = new Exchanger<>();

        for (int i = 0; i < 10; i++) {
            new Thread(new PutThread(ex, count)).start();
            new Thread(new GetThread(ex, count)).start();
        }

        /*Exchanger<String> exchanger = new Exchanger<>();

        new Thread(() -> {
            try {
                String message = "Hello from Thread 1";
                String response = exchanger.exchange(message);
                System.out.println("Thread 1 received: " + response);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        new Thread(() -> {
            try {
                String message = "Hello from Thread 2";
                String response = exchanger.exchange(message);
                System.out.println("Thread 2 received: " + response);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();*/
    }
}

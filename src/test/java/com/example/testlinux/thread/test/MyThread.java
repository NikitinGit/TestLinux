package com.example.testlinux.thread.test;

public class MyThread extends Thread {
    public void run() {
        // Код для выполнения в отдельном потоке
        System.out.println("Поток " + getName() + " начал выполнение.");
    }
}

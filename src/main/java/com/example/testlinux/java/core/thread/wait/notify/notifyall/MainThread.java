package com.example.testlinux.java.core.thread.wait.notify.notifyall;

public class MainThread {
    public static void main(String[] args) {
        Store store = new Store();
        Thread producerThread = new Thread(new Producer(store));
        Thread consumerThread = new Thread(new Consumer(store));
        producerThread.start();
        consumerThread.start();
    }
}

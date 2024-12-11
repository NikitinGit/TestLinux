package com.example.testlinux.service.thread.semaphore;

import java.util.concurrent.Semaphore;
public class Program {
    public static void main(String[] args) {
        Semaphore sem = new Semaphore(4);
        for(int i=1;i<6;i++) {
            new Philosopher(sem, i).start();
        }
    }
}

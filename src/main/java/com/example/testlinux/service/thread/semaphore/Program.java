package com.example.testlinux.service.thread.semaphore;

import java.util.concurrent.Semaphore;
public class Program {
    public static void main(String[] args) {
        Semaphore sem = new Semaphore(4);
        for(int i=1;i<6;i++) {
            Philosopher philosopher = new Philosopher(sem, i);
            philosopher.start();
            /*try {
                Thread.sleep(1652);
                philosopher.interrupt();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }
}

package com.example.testlinux.service.thread;

public class ThreadDemon {
    public static void main(String[] args){
        Thread thread1 = new Thread(() -> {

            int i = 0;
            while (++i < 10){
                if (i >2) {
                    return;
                }
                try {
                    System.out.println("Thread Deamon 1 is running i: " + i);
                    Thread.sleep(500);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        thread1.setDaemon(true);
        thread1.start();

        Thread thread2 = new Thread(() -> {
            int i = 0;
            while (++i < 10){
                try {
                    System.out.println("Thread Deamon 2, i: " + i);
                    Thread.sleep(200);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        thread2.start();
    }
}

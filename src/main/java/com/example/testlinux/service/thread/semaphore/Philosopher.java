package com.example.testlinux.service.thread.semaphore;

import java.util.concurrent.Semaphore;

// класс философа
public class Philosopher extends Thread {
    private final Semaphore sem; // семафор. ограничивающий число философов
    // кол-во приемов пищи
    int num = 0;
    // условный номер философа
    int id;

    // в качестве параметров конструктора передаем идентификатор философа и семафор
    Philosopher(Semaphore sem, int id) {
        this.sem = sem;
        this.id = id;
    }

    public void run() {
        try {
            while (num < 3) {
                //Запрашиваем у семафора разрешение на выполнение
                sem.acquire();
                System.out.println("Философ " + id + " садится за стол sem.availablePermits(): " + sem.availablePermits());
                // философ ест
                sleep(500);
                num++;

                System.out.println("Философ " + id + " выходит из-за стола sem.availablePermits(): " + sem.availablePermits());
                sem.release();

                // философ гуляет
                sleep(500);
            }
        } catch (InterruptedException e) {
            System.out.println("у философа " + id + " проблемы со здоровьем sem; " + sem.availablePermits());
        }
    }
}
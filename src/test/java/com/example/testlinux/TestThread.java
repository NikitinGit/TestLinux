package com.example.testlinux;

import com.example.testlinux.thread.test.MyObject;
import com.example.testlinux.thread.test.MyRunnable;
import com.example.testlinux.thread.test.MyThread;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class TestThread {
    @Test
    public void testThread(){
        // Создание нового потока
        MyThread myThread = new MyThread();
        // Запуск нового потока
        myThread.start();

        System.out.println("Основной поток продолжает выполнение.");
    }
    @Test
    public void testMyThread(){
        Thread thread = new Thread(new MyRunnable());
        // Неправильный способ — вызов run() напрямую
        // thread.run(); // Это выполнится в текущем потоке
        thread.start();
        System.out.println("Основной поток продолжает выполнение.");
    }
    @Test
    public void testThreadJoin() {
        Thread thread1 = new Thread(() -> {
            System.out.println("Thread 1 is running");
            try {
                Thread.sleep(555); // Имитация работы
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread 1 has finished");
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("Thread 2 is running");
            try {
                thread1.join(); // Ожидаем завершения thread1
                Thread.sleep(5); // Имитация работы
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread 2 has finished");
        });

        thread1.start();
        thread2.start();

        try {
            thread2.join(); // Ожидаем завершения thread2
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Both threads have finished. Main thread resumes.");
    }

    private volatile int count = 0;
    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    @Test
    public void testThread2(){
        MyObject myObject = new MyObject();
        long start = System.currentTimeMillis();
        for (int j = 0; j < 500000; j++) {
            Thread thread = new Thread(() -> {
                synchronized (this) {
                    count++;
                }
                //count++;
                //atomicInteger.incrementAndGet();
                //myObject.count++;
            });
            thread.start();
        }

        System.out.println("(System.currentTimeMillis() - start); " + (System.currentTimeMillis() - start));
        System.out.println("count; " + count + " atomicInteger.get() = " + atomicInteger.get() + ", myObject.count; " + myObject.count);
    }
}

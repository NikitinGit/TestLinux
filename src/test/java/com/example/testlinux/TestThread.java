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
                Thread.sleep(2555); // Имитация работы
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread 1 has finished");
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("Thread 2 is running");
            try {
                Thread.sleep(5); // Имитация работы
                thread1.join(255); // Ожидаем завершения thread1
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

    @Test
    public void testThreadLifeCircle(){
        Thread thread = new Thread(() -> {
            System.out.println("Мы находимся в методе 'run'.");
        });

        System.out.println("Состояние потока после создания: " + thread.getState());  // NEW

        thread.start();
        System.out.println("Состояние потока после вызова start(): " + thread.getState());  // RUNNABLE

        // Дадим потоку время для завершения
        try {
            Thread.sleep(100);  // Переход основного потока в TIMED_WAITING
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Состояние потока после завершения: " + thread.getState());
    }

    @Test
    public void testThreadInterupted(){
        Thread thread1 = new Thread(() -> {
            int i = 0;
            while (true){
                try {
                   Thread.sleep(500);
                   System.out.println("Thread 1 is running i: " + ++i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            thread1.start();
            Thread.sleep(3000);
            thread1.interrupt();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("Both threads have finished. Main thread resumes.");
    }

    @Test
    public void testDeamonThread(){
        Thread thread1 = new Thread(() -> {
            int i = 0;
            while (++i < 10){
                try {
                    System.out.println("Thread Deamon 1 is running i: " + i);
                    Thread.sleep(500);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        //thread1.setDaemon(true);
        thread1.start();

        try {
            Thread.sleep(3500);
        }catch (Exception e){}
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

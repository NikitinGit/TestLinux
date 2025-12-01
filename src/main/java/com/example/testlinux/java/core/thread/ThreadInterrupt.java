package com.example.testlinux.java.core.thread;

public class ThreadInterrupt {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.printf("%s started... \n", Thread.currentThread().getName());
            int counter=1; // счетчик циклов
            while(!Thread.currentThread().isInterrupted()){

                System.out.println("Loop " + counter++);
                try{
                    Thread.sleep(1000);
                }
                catch(InterruptedException e){
                    System.out.println(Thread.currentThread().getName() + " has been interrupted");
                }
            }
            System.out.printf("%s finished... \n", Thread.currentThread().getName());
        });
        thread.start();

        try{
            Thread.sleep(1000);
            thread.notify();
            thread.interrupt();
        }catch (InterruptedException e){}
    }

    private static void interruptedBlocking(){
        Thread blockingThread = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    System.out.println("Блокирующий поток работает... " + i);
                    // Поток засыпает на 1 секунду
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                // Поток прерван во время блокирующей операции
                System.out.println("Блокирующий поток был прерван.");
                System.out.println("Thread.currentThread().isInterrupted() 1; " + Thread.currentThread().isInterrupted());
                Thread.currentThread().interrupt(); // Восстанавливаем флаг прерывания
                System.out.println("Thread.currentThread().isInterrupted() 2; " + Thread.currentThread().isInterrupted());
                //return;  // Завершаем работу потока
            }
            System.out.println("Блокирующий поток завершил свою работу.");
        });

        blockingThread.start();

        // Прерывание потока через 3 секунды
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        blockingThread.interrupt();
    }

    private static void isAlive(){
        System.out.println("Main thread started...");
        MyThread1 myThread1 = new MyThread1();
        new Thread(myThread1,"MyThread Nikitin").start();

        try{
            Thread.sleep(1100);

            myThread1.disable();

            Thread.sleep(1000);
        }
        catch(InterruptedException e){
            System.out.println("Thread has been interrupted");
        }
        System.out.println("Main thread finished...");
    }

    private static void testThread2Interrupted (){
        MyThread2 myThread2 = new MyThread2();
        Thread thread = new Thread(myThread2,"Nikitn Thread"); // второй парметр устанавливает имя текущего потока, которое можно получить в классе MyThread2
        thread.start();
        try {
            Thread.sleep(1100);
            thread.interrupt();
        }catch (InterruptedException e){}
    }
}

package com.example.testlinux.service.thread.wait.notify.notifyall;

public class Store {
    private int product;
    public synchronized void get(){
        while (product < 1){
            try {
                wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        product--;
        System.out.println("Покупатель купил товар, Товаров на складе: " + product);
        notify();
    }

    public synchronized void put(){
        while (product >= 3){
            try {
                wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        product++;
        System.out.println("Производитель добавил товар, Товаров на складе: " + product);
        notify();
    }
}

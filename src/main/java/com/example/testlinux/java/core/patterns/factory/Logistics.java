package com.example.testlinux.java.core.patterns.factory;

public abstract class Logistics {

    // состояние — интерфейс не может хранить поля с реальными значениями
    private final String region;

    protected Logistics(String region) {
        System.out.println("Logistics(String region): " + region);
        this.region = region;
    }

    abstract Transport createTransport(); // фабричный метод — подкласс обязан реализовать

    // общая логика: оба подкласса используют этот метод БЕЗ переопределения
    public void deliver(String name) {
        System.out.println("Logistics.deliver(), this.region;  " + this.region);           // используем поле
        System.out.println("before Transport t = createTransport(); ..." + name);
        Transport t = createTransport(); // вызывает фабричный метод
        t.move();
        System.out.println("Delivered!");
    }
}

package com.example.testlinux.java.core.factory;

public abstract class Logistics {
    abstract Transport createTransport(); // фабричный метод — подкласс обязан реализовать

    // общая логика: оба подкласса используют этот метод БЕЗ переопределения
    void deliver(String name) {
        System.out.println("before Transport t = createTransport(); ..." + name);
        Transport t = createTransport(); // вызывает фабричный метод
        t.move();
        System.out.println("Delivered!");
    }
}

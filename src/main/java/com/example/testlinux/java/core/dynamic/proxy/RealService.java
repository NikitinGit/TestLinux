package com.example.testlinux.java.core.dynamic.proxy;

public class RealService implements Service {
    public void executeAction(String objectName) {
        System.out.println("Выполнение действия с объектом: " + objectName);
    }
}
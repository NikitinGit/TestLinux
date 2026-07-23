package com.example.testlinux.java.core.dynamic.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyHandler  implements InvocationHandler {
    private final Object realObject;

    public ProxyHandler(Object realObject) {
        this.realObject = realObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Логика ДО вызова метода (Перехват)
        System.out.println("[ПЕРЕХВАТ] Логирование до вызова метода: " + method.getName());

        // Вызов оригинального метода на реальном объекте
        Object result = method.invoke(realObject, args);

        // Логика ПОСЛЕ вызова метода
        System.out.println("[ПЕРЕХВАТ] Логирование после вызова метода.");
        return result;
    }
}
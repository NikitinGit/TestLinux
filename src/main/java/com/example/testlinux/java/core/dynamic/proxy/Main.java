package com.example.testlinux.java.core.dynamic.proxy;

import java.lang.reflect.Proxy;

public class Main {
    public static void main(String[] args) {
        Service realService = new RealService();

        // Создаем прокси-заместитель
        Service proxyService = (Service) Proxy.newProxyInstance(
                Service.class.getClassLoader(),
                new Class<?>[]{Service.class},
                new ProxyHandler(realService)
        );

        // Вызываем метод через прокси
        proxyService.executeAction("База Данных");
    }
}
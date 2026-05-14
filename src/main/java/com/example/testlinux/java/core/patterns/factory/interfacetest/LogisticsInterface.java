package com.example.testlinux.java.core.patterns.factory.interfacetest;

import com.example.testlinux.java.core.patterns.factory.method.Transport;

public interface LogisticsInterface {

    // ПОПЫТКА 1: объявить поле напрямую
    // Компилятор автоматически делает его: public static final String region = ???
    // static — одно на всех, не у каждого объекта своё
    // final — значение нельзя изменить после присвоения
    // Нельзя написать просто: String region; — компилятор требует сразу присвоить значение
    // String region;              // ОШИБКА: Variable 'region' might not have been initialized

    // ПОПЫТКА 2: присвоить значение — но оно будет одно на всех
    // String region = "общее";    // компилируется, но это static final — не состояние объекта:
                                   // new RoadLogisticsI("Москва") и new SeaLogisticsI("Владивосток")
                                   // оба вернут "общее" — передать своё значение невозможно

    // ПОПЫТКА 3: private поле
    // private String region;      // ОШИБКА: Interface fields cannot be private — поле обязано быть public static final

    Transport createTransport();

    default void deliver(String name) {
        // System.out.println("Region: " + region); // если раскомментировать ПОПЫТКУ 2 —
                                                     // всегда напечатает "общее", для всех объектов одинаково
        Transport t = createTransport();
        t.move();
        System.out.println("Delivered!");
    }
}
package com.example.testlinux.java.core.patterns.singlton;

/**
 * 3) enum-синглтон — самый простой и надёжный (рекомендует Дж. Блох, Effective Java).
 * <p>
 * Потокобезопасен и инициализируется лениво на уровне JVM (enum-константа = static final поле
 * класса enum, создаётся при инициализации класса — потокобезопасно by design).
 * <p>
 * Бонусы «из коробки»: защита от рефлексии (нельзя создать второй экземпляр через reflection)
 * и корректная сериализация (при десериализации не создаётся дубль).
 * <p>
 * Минусы: нельзя наследоваться; создаётся при первом обращении к классу enum (не через
 * отдельный ленивый метод, но на практике это то же самое).
 */
public enum SingletonEnum {

    INSTANCE;

    SingletonEnum() {
        System.out.println("SingletonEnum: конструктор (создан)");
    }

    public void doWork() {
        System.out.println("SingletonEnum.doWork()");
    }
}

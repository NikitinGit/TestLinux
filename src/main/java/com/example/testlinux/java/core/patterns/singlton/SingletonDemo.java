package com.example.testlinux.java.core.patterns.singlton;

/**
 * Демонстрация: каждый способ возвращает ОДИН И ТОТ ЖЕ экземпляр (==),
 * а конструктор вызывается ровно один раз (лениво — при первом обращении).
 */
public class SingletonDemo {

    public static void main(String[] args) {
        System.out.println("=== 1) DCL ===");
        SingletonDcl d1 = SingletonDcl.get();
        SingletonDcl d2 = SingletonDcl.get();       // конструктор второй раз НЕ вызовется
        System.out.println("d1 == d2 ? " + (d1 == d2));

        System.out.println("=== 2) Holder ===");
        SingletonHolder h1 = SingletonHolder.getInstance();
        SingletonHolder h2 = SingletonHolder.getInstance();
        System.out.println("h1 == h2 ? " + (h1 == h2));

        System.out.println("=== 3) Enum ===");
        SingletonEnum e1 = SingletonEnum.INSTANCE;
        SingletonEnum e2 = SingletonEnum.INSTANCE;
        System.out.println("e1 == e2 ? " + (e1 == e2));
        e1.doWork();
    }
}

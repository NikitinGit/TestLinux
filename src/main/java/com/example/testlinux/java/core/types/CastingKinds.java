package com.example.testlinux.java.core.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Виды кастов в Java.
 *   1. Upcast      — к супертипу (всегда безопасен, обычно неявный)
 *   2. Downcast    — к подтипу (требует явного каста, проверяется в runtime -> ClassCastException)
 *   3. Sidecast    — между несвязанными интерфейсами
 *   4. Widening    — расширение примитива (int -> long)
 *   5. Narrowing   — сужение примитива (double -> int), возможна потеря
 *   6. Boxing/Unboxing — примитив <-> обёртка
 *   7. Unchecked   — generics (стираются в runtime)
 *   8. Class.cast  — программный аналог (T) obj
 *   9. Pattern matching for instanceof (Java 16+) — безопасная альтернатива
 */
public class CastingKinds {

    static class Animal { void execute() { System.out.println("Animal"); } }
    static class Dog extends Animal implements Runnable {
        @Override void execute() { System.out.println("Dog"); }
        @Override public void run() { System.out.println("Dog runs"); }
    }

    public static void main(String[] args) {

        // 1. Upcast — неявный
        Dog dog = new Dog();
        Animal a = dog;                 // implicit upcast
        Animal a2 = (Animal) dog;       // явный — то же самое
        a.execute();                    // вызывается Dog.execute() — динамическая диспетчеризация
        a2.execute();

        // 2. Downcast — нужен явный (Dog)
        Animal animal = new Dog();      // реальный тип объекта — Dog
        Dog d = (Dog) animal;           // OK, фактически это Dog
        d.execute();

        // 2b. Downcast, который упадёт в runtime
        Animal pureAnimal = new Animal();
        try {
            Dog bad = (Dog) pureAnimal; // ClassCastException
            bad.run();
        } catch (ClassCastException e) {
            System.out.println("CCE: " + e.getMessage());
        }

        // 3. Sidecast — между несвязанными интерфейсами через общий объект
        Runnable r = new Dog();         // Dog implements Runnable
        Animal viaSide = (Animal) r;    // тот же объект — он же Animal
        viaSide.execute();

        // 4. Widening (расширение) — неявно
        int n = 10;
        long l = n;                     // int -> long, без потерь
        double dbl = n;                 // int -> double
        System.out.println("widening: " + l + ", " + dbl);

        // 5. Narrowing (сужение) — явно, возможна потеря
        double pi = 3.99;
        int trunc = (int) pi;           // 3, дробная часть отброшена
        long big = 5_000_000_000L;
        int overflow = (int) big;       // переполнение
        System.out.println("narrowing: " + trunc + ", overflow=" + overflow);

        // 6. Boxing / unboxing
        Integer boxed = 42;             // autoboxing -> Integer.valueOf(42)
        int unboxed = boxed;            // auto-unboxing -> boxed.intValue()
        System.out.println("boxing: " + boxed + " / " + unboxed);

        // 7. Unchecked cast в generics — компилятор предупреждает (warning)
        Object obj = new ArrayList<String>();
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) obj;   // в runtime проверяется только List, не <String>
        System.out.println("unchecked cast: " + list.getClass().getSimpleName());

        // 8. Class.cast — программный аналог
        Object o = new Dog();
        Dog d2 = Dog.class.cast(o);     // если не Dog -> ClassCastException
        d2.execute();

        // 9. Pattern matching for instanceof (Java 16+) — без явного каста
        Object some = new Dog();
        if (some instanceof Dog small) {
            small.run();                // small уже типа Dog
        }

        // Бонус: каст-операция работает и для интерфейсов
        Serializable ser = (Serializable) new ArrayList<Integer>();
        System.out.println("ArrayList is Serializable: " + ser);
    }
}
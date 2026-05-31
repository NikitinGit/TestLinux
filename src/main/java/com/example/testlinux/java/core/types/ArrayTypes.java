package com.example.testlinux.java.core.types;

import java.io.Serializable;
import java.lang.reflect.Array;

/**
 * Массив — полноценный ссылочный тип.
 *   - У каждой размерности свой Class-объект (в отличие от generics, тут erasure нет)
 *   - Все массивы — подтипы Object, Cloneable, Serializable
 *   - Имена в JVM: [I = int[], [[I = int[][], [Lpkg.Cls; = Cls[]
 *   - Массивы КОВАРИАНТНЫ: Dog[] is-a Animal[] -> риск ArrayStoreException
 */
public class ArrayTypes {

    static class Animal {}
    static class Dog extends Animal {}
    static class Cat extends Animal {}

    public static void main(String[] args) {

        // 1. Разные размерности — разные ТИПЫ и разные классы
        int[]     a1 = new int[3];
        int[][]   a2 = new int[3][3];
        int[][][] a3 = new int[3][3][3];
        System.out.println("int[].class       = " + a1.getClass());   // class [I
        System.out.println("int[][].class     = " + a2.getClass());   // class [[I
        System.out.println("int[][][].class   = " + a3.getClass());   // class [[[I
        System.out.println("разные? " + ((Class<?>) a1.getClass() != (Class<?>) a2.getClass()));

        // 2. Каждый element-тип -> свой Class массива
        System.out.println("Dog[].class    = " + Dog[].class);        // [Lcom...Dog;
        System.out.println("Animal[].class = " + Animal[].class);
        System.out.println("разные классы? " + ((Class<?>) Dog[].class != (Class<?>) Animal[].class));

        // 3. Массив — это Object, Cloneable, Serializable
        Object     o = a1;
        Cloneable  c = a1;
        Serializable s = a1;
        System.out.println("массив — это Object/Cloneable/Serializable: "
                + (o != null && c != null && s != null));

        // 4. Поле length и метод clone() — синтаксическая магия
        int[] copy = a1.clone();
        System.out.println("clone OK, length=" + copy.length);

        // 5. Ковариантность массивов
        Dog[] dogs = { new Dog(), new Dog() };
        Animal[] animals = dogs;            // OK — Dog[] подтип Animal[]
        animals[0] = new Dog();             // OK
        try {
            animals[0] = new Cat();         // ArrayStoreException — фактически массив Dog'ов
        } catch (ArrayStoreException e) {
            System.out.println("ArrayStoreException: " + e.getMessage());
        }

        // 6. Создание массива неизвестного типа через reflection
        Object dynamic = Array.newInstance(Dog.class, 5);
        System.out.println("Array.newInstance -> " + dynamic.getClass());

        // 7. Массивы примитивов и обёрток — разные типы!
        int[]     primInts = new int[]{1, 2, 3};
        Integer[] boxedInts = new Integer[]{1, 2, 3};
        System.out.println("int[] != Integer[]: " + ((Class<?>) primInts.getClass() != (Class<?>) boxedInts.getClass()));
        // int[] нельзя присвоить Object[], а Integer[] — можно
        // Object[] bad = primInts;  // compile error
        Object[] ok = boxedInts;
        System.out.println("Object[] from Integer[]: " + ok.length);
    }
}
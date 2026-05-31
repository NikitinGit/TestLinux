package com.example.testlinux.java.core.types;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс != тип.
 * Класс — синтаксическая конструкция в исходниках; в runtime ему соответствует один Class-объект.
 * Тип — категория системы типов компилятора. Типов больше, чем классов:
 *   - примитивы (int, long, ...) — типы без классов
 *   - интерфейсы — типы без классов
 *   - массивы — типы без объявления в .java
 *   - параметризованные типы (List<String>) — много типов на один класс
 */
public class ClassVsType {

    interface Animal { void execute(); }
    static class Dog implements Animal { public void execute() { System.out.println("Woof"); } }

    public static void main(String[] args) {
        // 1. Примитив — это тип, но не класс
        int x = 5;
        // int.class существует, но это псевдо-Class для дескриптора, не настоящий класс
        System.out.println("int.class = " + int.class + ", x = " + x);

        // 2. Один объект — несколько типов
        Dog d = new Dog();
        Animal a = d;          // тип переменной — Animal (интерфейс)
        Object o = d;          // тип переменной — Object
        System.out.println("один объект, три типа переменной — "
                + "Dog: " + d.getClass().getSimpleName()
                + ", Animal: " + a
                + ", Object: " + o.getClass().getSimpleName());

        // 3. Один класс — много параметризованных типов (generics стираются)
        ArrayList<String>  s = new ArrayList<>();
        ArrayList<Integer> i = new ArrayList<>();
        System.out.println("ArrayList<String> и ArrayList<Integer> — РАЗНЫЕ типы, но ОДИН класс: "
                + (s.getClass() == i.getClass()));   // true

        // 4. List — тип-интерфейс, ArrayList — тип-класс. Подтип-отношения работают:
        List<String> list = s;     // ArrayList<String> -> List<String>
        System.out.println("List.isInstance(ArrayList) = " + List.class.isInstance(s));
    }
}
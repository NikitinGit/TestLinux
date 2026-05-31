package com.example.testlinux.java.core.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Один класс ArrayList — бесконечно много ТИПОВ:
 *   ArrayList<String>, ArrayList<Integer>, ArrayList<List<Dog>>, ArrayList (raw), ArrayList<?>, ArrayList<? extends Animal>...
 * В runtime все они — один и тот же класс (type erasure).
 *
 * Типы-кирпичики generics:
 *   - параметризованный тип: List<String>
 *   - raw type:              List
 *   - wildcard:              List<?>, List<? extends T>, List<? super T>
 *   - type variable:         T, E, K, V (в объявлении класса/метода)
 */
public class GenericTypes {

    static class Animal { @Override public String toString() { return "Animal"; } }
    static class Dog extends Animal { @Override public String toString() { return "Dog"; } }

    // Type variable T живёт только на уровне сигнатуры — в runtime его нет
    static <T> T firstOrNull(List<T> list) {
        return list.isEmpty() ? null : list.get(0);
    }

    // Bounded type variable: T extends Animal
    static <T extends Animal> void describe(T t) {
        System.out.println("describe " + t);
    }

    public static void main(String[] args) {

        // 1. Разные параметризованные типы -> один runtime-класс
        List<String>  ls = new ArrayList<>(2);
        //ls.add("bin");
        List<Integer> li = new ArrayList<>(5);
        //li.add(2);
        System.out.println("(ls.getClass() == li.getClass(): " + (ls.getClass() == li.getClass())
        + ", ls == li: " + (ls.equals(li)));  // true
        // А с массивами было бы наоборот — там типы реально разные в runtime

        // 2. Raw type — обходит проверку типов, выдаёт warning
        List raw = new ArrayList();
        raw.add("string");
        raw.add(42);                     // компилятор не возражает
        System.out.println("raw: " + raw);

        // 3. Из-за erasure нельзя проверить параметризованный тип
        Object o = new ArrayList<String>();
        System.out.println("o instanceof ArrayList: " + (o instanceof ArrayList));
        // o instanceof ArrayList<String>   // compile error: нельзя

        // 4. Wildcard ? — «какой-то неизвестный конкретный тип»
        List<?> unknown = ls;            // можем присвоить любой List<X>
        // unknown.add("hi");            // нельзя — мы не знаем, чего именно ждёт список
        System.out.println("unknown.size() = " + unknown.size());

        // 5. Type variable в методе
        Integer first = firstOrNull(Arrays.asList(1, 2, 3));
        System.out.println("firstOrNull: " + first);

        describe(new Dog());             // T = Dog
        describe(new Animal());          // T = Animal

        // 6. Параметризованный тип внутри параметризованного — снова новый тип
        List<List<Dog>> nested = new ArrayList<>();
        nested.add(Arrays.asList(new Dog(), new Dog()));
        System.out.println("nested[0].size = " + nested.get(0).size());
    }
}
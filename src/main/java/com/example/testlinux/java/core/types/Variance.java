package com.example.testlinux.java.core.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Вариативность (variance) — как отношения подтипов "поднимаются" в параметризованных типах.
 *
 *   Пусть Dog <: Animal (Dog — подтип Animal).
 *
 *   ИНВАРИАНТНОСТЬ:       List<Dog>            НЕ подтип List<Animal>
 *   КОВАРИАНТНОСТЬ:       List<? extends Animal>  принимает List<Dog>, List<Cat>, List<Animal>
 *                         (можно ЧИТАТЬ как Animal, нельзя ДОБАВЛЯТЬ)
 *   КОНТРАВАРИАНТНОСТЬ:   List<? super Dog>    принимает List<Dog>, List<Animal>, List<Object>
 *                         (можно ДОБАВЛЯТЬ Dog, читать только как Object)
 *
 *   Правило PECS: Producer Extends, Consumer Super.
 *      - Если коллекция отдаёт элементы — ? extends T
 *      - Если коллекция принимает элементы — ? super T
 *
 * Массивы в Java — ковариантны "by design" (с риском ArrayStoreException, см. ArrayTypes).
 * Generics — инвариантны по умолчанию, ко/контравариантность задаётся wildcard'ами в месте использования.
 */
public class Variance {

    static class Animal { @Override public String toString() { return "Animal"; } }
    static class Dog extends Animal { @Override public String toString() { return "Dog"; } }
    static class Puppy extends Dog { @Override public String toString() { return "Puppy"; } }

    // ---------- ИНВАРИАНТНОСТЬ ----------
    static void invariantDemo() {
        List<Dog> dogs = new ArrayList<>();
        dogs.add(new Dog());

        // List<Animal> animals = dogs;   // compile error — инвариантно
        // если бы было можно — мы бы добавили туда Cat и сломали типизацию
    }

    // ---------- КОВАРИАНТНОСТЬ: ? extends T (PRODUCER) ----------
    // Метод "читает" из списка — может принимать List любого подтипа Animal
    static void printAll(List<? extends Animal> animals) {
        for (Animal a : animals) {       // читаем как Animal — безопасно
            System.out.println("read: " + a);
        }
        // animals.add(new Dog());        // compile error: запись запрещена
        // в список реально может лежать List<Puppy>, добавлять туда Dog нельзя
    }

    // ---------- КОНТРАВАРИАНТНОСТЬ: ? super T (CONSUMER) ----------
    // Метод "кладёт" Dog'ов — может принимать List Dog/Animal/Object
    static void addDogs(List<? super Dog> sink) {
        sink.add(new Dog());             // ОК — Dog влезет и в List<Animal>, и в List<Object>
        sink.add(new Puppy());           // Puppy — подтип Dog, тоже ОК
        // Animal a = sink.get(0);       // нельзя — мы не знаем, может это List<Object>
        Object o = sink.get(0);          // читать можно только как Object
        System.out.println("sink[0] as Object: " + o);
    }

    public static void main(String[] args) {

        // --- Ковариантность массивов (Java делает это автоматически) ---
        Dog[] dogArr = { new Dog(), new Puppy() };
        Animal[] animArr = dogArr;       // OK: Dog[] <: Animal[]
        System.out.println("covariant array: " + animArr[0]);
        try {
            animArr[0] = new Animal();   // компилятор разрешит, runtime — нет
        } catch (ArrayStoreException e) {
            System.out.println("ArrayStoreException: реальный тип массива — Dog[]");
        }

        // --- Generics инвариантны: РАЗНЫЕ типы ---
        List<Dog> dogs = new ArrayList<>(Arrays.asList(new Dog(), new Puppy()));
        // List<Animal> animals = dogs;  // compile error

        // --- ? extends — ковариантно, читаем ---
        printAll(dogs);                  // List<Dog> подходит под List<? extends Animal>
        printAll(Arrays.asList(new Animal(), new Dog()));
        printAll(Arrays.asList(new Puppy()));

        // --- ? super — контравариантно, пишем ---
        List<Animal> animalSink = new ArrayList<>();
        addDogs(animalSink);             // List<Animal> подходит под List<? super Dog>
        System.out.println("animalSink = " + animalSink);

        List<Object> objectSink = new ArrayList<>();
        addDogs(objectSink);             // List<Object> тоже подходит
        System.out.println("objectSink = " + objectSink);

        // List<Puppy> puppySink = ...   // НЕ подходит под ? super Dog (Puppy уже Dog)

        // --- Контравариантность для функциональных интерфейсов ---
        // Consumer<? super Dog>: можно скормить Consumer<Animal>, он умеет "потреблять" и Dog
        Consumer<Animal> printAnimal = a -> System.out.println("consumer got " + a);
        Consumer<? super Dog> dogConsumer = printAnimal;   // контравариантность по параметру
        dogConsumer.accept(new Dog());
        dogConsumer.accept(new Puppy());

        // Function<? super Dog, ? extends Animal>: контравариантна по входу, ковариантна по выходу
        Function<Animal, Dog> animalToDog = a -> new Dog();
        Function<? super Dog, ? extends Animal> f = animalToDog;
        Animal out = f.apply(new Puppy());
        System.out.println("function out: " + out);

        // --- Реальный пример контравариантности: Comparator ---
        // Comparator<Animal> подойдёт там, где нужен Comparator<Dog>:
        Comparator<Animal> byHashCode = Comparator.comparingInt(Object::hashCode);
        Comparator<? super Dog> dogCmp = byHashCode;     // контравариантно
        List<Dog> ds = new ArrayList<>(Arrays.asList(new Dog(), new Dog()));
        ds.sort(dogCmp);
        System.out.println("sorted dogs OK");

        invariantDemo();
    }
}

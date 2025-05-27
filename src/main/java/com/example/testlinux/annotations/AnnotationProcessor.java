package com.example.testlinux.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class AnnotationProcessor {
    public static void main(String[] args) {
        System.out.println("third-branch commit 1");
        Person pers = new Person(); System.out.println("it not work pers.getAge(); " + pers.getAge());
        // Получаем все поля класса Person
        test(UnKnown.class);
        createPersonWithoutNew();

        try {
            Constructor<UnKnown> constructor = UnKnown.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            UnKnown unKnown = constructor.newInstance();

            Field fieldAge = UnKnown.class.getDeclaredField("age");
            fieldAge.setAccessible(true);
            fieldAge.set(unKnown, fieldAge.getAnnotation(MyAnnotation.class).length());

            Field fieldName = UnKnown.class.getDeclaredField("name");
            fieldName.setAccessible(true);
            fieldName.set(unKnown, fieldName.getAnnotation(MyAnnotation.class).name());

            System.out.println("unKnown.getName(); " + unKnown.getName());
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    //@Deprecated(since = "disable update is best practice of devops sre by emurashin.ru", forRemoval = true)
    static void test(Class<?> classTest) {
        try {
            Field[] fields = classTest.getDeclaredFields();
            Constructor<?> unKnownConstructor = classTest.getDeclaredConstructor();
            for (Field field : fields) {
                // Проверяем, есть ли у поля аннотация MyAnnotation
                if (field.isAnnotationPresent(MyAnnotation.class)) {
                    MyAnnotation annotation = field.getAnnotation(MyAnnotation.class);
                    System.out.println("Field: " + field.getName());
                    System.out.println("Annotation name: " + annotation.name());
                    System.out.println("Annotation length: " + annotation.length());
                    System.out.println("-------------------");
                }
            }
        }catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    static void createPersonWithoutNew() {
        // Создаем объект Person без new (через рефлексию)
        try {
            Person person = Person.class.getDeclaredConstructor().newInstance();
            // Получаем все поля класса
            Field[] fields = Person.class.getDeclaredFields();
            // Проходим по всем полям и устанавливаем значения, если есть аннотация @MyAnnotation
            for (Field field : fields) {
                if (field.isAnnotationPresent(MyAnnotation.class)) {
                    MyAnnotation annotation = field.getAnnotation(MyAnnotation.class);

                    // Разрешаем доступ к private-полям
                    field.setAccessible(true);

                    // Устанавливаем значения в зависимости от типа поля
                    if (field.getType() == String.class) {
                        field.set(person, annotation.name());  // Используем name() из аннотации
                    } else if (field.getType() == int.class) {
                        field.setInt(person, annotation.length());  // Используем length() из аннотации
                    }
                }
            }
            // Проверяем, что поля заполнились
            System.out.println("Name: " + person.getName());  // Выведет "person_name" (из аннотации)
            System.out.println("Age: " + person.getAge());    // Выведет 125 (значение по умолчанию)
            System.out.println("Address: " + person.getAddress());  // Выведет "" (пустая строка)
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.example.testlinux.annotations;

import java.lang.reflect.Field;

public class AnnotationProcessor {
    public static void main(String[] args) {

        Person pers = new Person();
        System.out.println("it not work pers.getAge(); " + pers.getAge());
        // Получаем все поля класса Person
        Field[] fields = Person.class.getDeclaredFields();

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
        test(UnKnown.class);
    }

    @Deprecated(since = "disable update is best practice of devops sre by emurashin.ru")
    public static void test(Class<?> classTest) {
        Field[] fields = classTest.getDeclaredFields();

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
    }
}

package com.example.testlinux.unit.test;

public class StackExample {
    public static void main(String[] args) {
        method1();
    }

    public static void method1() {
        method2();
    }

    public static void method2() {
        method3();
    }

    public static void method3() {
        // Выводим информацию о стеке вызовов
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            System.out.println(element.getClassName() + " - " + element.getMethodName());
        }
    }


}

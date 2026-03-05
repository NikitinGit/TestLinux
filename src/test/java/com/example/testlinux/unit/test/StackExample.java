package com.example.testlinux.unit.test;

import com.example.testlinux.stream.api.Stage1;

import java.util.stream.Stream;

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

        System.out.println("Thread.currentThread().getStackTrace().length; " + Thread.currentThread().getStackTrace().length);
    }


}

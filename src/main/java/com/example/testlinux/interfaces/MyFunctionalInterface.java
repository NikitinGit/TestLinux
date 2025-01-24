package com.example.testlinux.interfaces;

import java.io.IOException;

@FunctionalInterface
public interface MyFunctionalInterface {

    void execute() throws IOException;

    default void defaultN1() {
        System.out.println("MyFunctionalInterface defaultN1()");
    }

    default void defaultN2() {
        System.out.println("MyFunctionalInterface defaultN2()");
    }

    static void staticN1() {
        System.out.println("MyFunctionalInterface staticN1()");
    }

    static void staticN2() {
        System.out.println("MyFunctionalInterface staticN2()");
    }
    //void error();
}

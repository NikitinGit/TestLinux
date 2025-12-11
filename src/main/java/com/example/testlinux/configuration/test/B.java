package com.example.testlinux.configuration.test;


public class B {
    private final A a;
    public B(A a) {
        this.a = a;
        System.out.println("B: injected A → " + a.hashCode());
    }
}

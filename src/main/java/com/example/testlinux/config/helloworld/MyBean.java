package com.example.testlinux.config.helloworld;

public class MyBean {
    int number = 25;

    public MyBean(int number) {
        this.number = number;
        System.out.println("Create Bean!, number; " + number);
    }

    public void sayHello() {
        System.out.println("Hello from MyBean Nikitin!" + this + ", number; " + number);
    }
}

package com.example.testlinux.annotations;

import lombok.Getter;


public class UnKnown {
    private UnKnown(){}

    @MyAnnotation(name = "UnKnown_name", length = 625)
    private String name = " ";

    @MyAnnotation(name = "UnKnown_age", length = 785)
    private int age;

    @MyAnnotation
    private String address;

    public String getName(){
        return name + age;
    }
}

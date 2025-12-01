package com.example.testlinux.java.core.annotations;

import lombok.Getter;

@Getter
public class Person {
    @MyAnnotation(name = "person_name", length = 100)
    private String name;

    @MyAnnotation(name = "person_age")
    private int age;

    @MyAnnotation
    private String address;
}

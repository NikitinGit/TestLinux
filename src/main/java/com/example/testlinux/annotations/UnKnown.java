package com.example.testlinux.annotations;

import lombok.Getter;

@Getter
public class UnKnown {
    @MyAnnotation(name = "UnKnown_name", length = 625)
    private String name;

    @MyAnnotation(name = "UnKnown_age", length = 785)
    private int age;

    @MyAnnotation
    private String address;
}

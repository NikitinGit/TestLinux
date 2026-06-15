package com.example.testlinux.java.core;

import java.util.Optional;

public class OptionalTest {

    public static void main(String[] args) {
        Optional<String> optional = Optional.of("Hello");
        Optional<String> optionalEmpty = Optional.empty();

        String result = optional.orElse(getDefaultValue());
        System.out.println("result; " + result);

        result = optional.orElseGet(OptionalTest::getDefaultValue);
        System.out.println("result getz; " + result);

        String resultEmpty = optionalEmpty.orElse(getDefaultValue());
        System.out.println("resultEmpty; " + resultEmpty);
    }

    private static String getDefaultValue() {
        System.out.println("Calculating default value...");
        return "Default";
    }
}

package com.example.testlinux.collections;

import java.util.ArrayList;
import java.util.List;

public class StreamApi {
    public static void main(String[] args) {
        List<Integer> sample = new ArrayList<>();
        sample.stream().forEach(System.out::println);
    }
}

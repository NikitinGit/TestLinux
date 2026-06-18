package com.example.testlinux.java.core.hashcodetest;

import com.example.testlinux.stream.api.Stage1;
import com.example.testlinux.stream.api.Stage2;

import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        HashSet<Person> set = new HashSet<>();
        set.add(new Person("Alice"));
        System.out.println(set.contains(new Person("Alice"))); // true, теперь работает правильно
        var stageN3 = new Stage2();
        System.out.println("hashCode After stageN3 created: " + stageN3.hashCode());

        var stageN125 = new Stage1();
        System.out.println("hashCode After stageN125 created: " + stageN125.hashCode());

        var stageN2 = new Stage1();
        System.out.println("hashCode After stageN2 created: " + stageN2.hashCode());
//        true
//        Stage1 hashCode(); 458209687
//        hashCode After stageN1 created: 458209687
//        Stage1 hashCode(); 897697267
//        hashCode After stageN2 created: 897697267
    }
}

package com.example.testlinux.java.core.patterns.factory.abstracts.classes;

import com.example.testlinux.java.core.patterns.factory.abstracts.interfaces.Truck;

public class TruckLinux implements Truck {
    public TruckLinux() {
        System.out.println("TruckLinux() constructor");
    }

    public void shoot() {
        System.out.println("TruckLinux shoot();");
    }
}

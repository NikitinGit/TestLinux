package com.example.testlinux.java.core.patterns.factory.abstracts.classes;

import com.example.testlinux.java.core.patterns.factory.abstracts.interfaces.Truck;

public class TruckWindow implements Truck {
    public TruckWindow() {
        System.out.println("TruckWindow() constructor");
    }

    public void shoot() {
        System.out.println("TruckWindow shoot();");
    }
}

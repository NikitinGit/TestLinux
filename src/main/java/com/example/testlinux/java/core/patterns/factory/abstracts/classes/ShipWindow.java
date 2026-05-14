package com.example.testlinux.java.core.patterns.factory.abstracts.classes;

import com.example.testlinux.java.core.patterns.factory.abstracts.interfaces.Ship;

public class ShipWindow implements Ship {
    public ShipWindow() {
        System.out.println("ShipWindow() constructor");
    }

    public void shoot() {
        System.out.println("ShipWindow shoot();");
    }
}

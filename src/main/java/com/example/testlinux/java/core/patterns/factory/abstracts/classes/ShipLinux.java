package com.example.testlinux.java.core.patterns.factory.abstracts.classes;

import com.example.testlinux.java.core.patterns.factory.abstracts.interfaces.Ship;

public class ShipLinux implements Ship {
    public ShipLinux() {
        System.out.println("ShipLinux() constructor");
    }

    public void shoot() {
        System.out.println("ShipLinux shoot();");
    }
}

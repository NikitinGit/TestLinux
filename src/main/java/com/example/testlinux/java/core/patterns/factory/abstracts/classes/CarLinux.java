package com.example.testlinux.java.core.patterns.factory.abstracts.classes;

import com.example.testlinux.java.core.patterns.factory.abstracts.interfaces.Car;

public class CarLinux implements Car {
    public CarLinux() {
        System.out.println("CarLinux() constructor");
    }

    @Override
    public void driving() {
        System.out.println("CarLinux driving();");
    }
}

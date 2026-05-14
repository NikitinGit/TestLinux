package com.example.testlinux.java.core.patterns.factory.abstracts.classes;

import com.example.testlinux.java.core.patterns.factory.abstracts.interfaces.Car;

public class CarWindow implements Car {
    public CarWindow() {
        System.out.println("CarWindow() constructor");
    }

    @Override
    public void driving() {
        System.out.println("CarWindow driving();");
    }
}

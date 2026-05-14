package com.example.testlinux.java.core.patterns.factory;

import com.example.testlinux.java.core.patterns.factory.abstracts.interfaces.TransportOSFactory;
import com.example.testlinux.java.core.patterns.factory.abstracts.classes.TransportLinuxFactory;
import com.example.testlinux.java.core.patterns.factory.abstracts.classes.TransportWindowFactory;
import com.example.testlinux.java.core.patterns.factory.method.RoadLogistics;
import com.example.testlinux.java.core.patterns.factory.method.SeaLogistics;
import com.example.testlinux.java.core.patterns.factory.simple.TransportFactory;

public class FactoryMain {
    public static void main(String[] stn) {
        /*factoryMethod();
        factorySimple();*/
        factoryAbstract();
    }

    public static void factorySimple() {
        System.out.println("factorySimple()---------------------------------------------------------------------------");
        var factory = new TransportFactory();
        factory.create(TransportType.SHIP).move();
        factory.create(TransportType.CAR).move();
        factory.create(TransportType.PLANE).move();
    }

    public static void factoryMethod() {
        System.out.println("factoryMethod()---------------------------------------------------------------------------");
        var test = new RoadLogistics("Москва");
        test.deliver("test RoadLogistics");

        var test2 = new SeaLogistics("Владивосток");
        test2.deliver("test SeaLogistics");
    }

    public static void factoryAbstract() {
        String osName = System.getProperty("os.name").toLowerCase();
        TransportOSFactory factory =  osName.startsWith("win")
                ? new TransportWindowFactory()
                : new TransportLinuxFactory();
        factory.createTruck().shoot();
        factory.createCar().driving();

        factory = new TransportWindowFactory();
        factory.createTruck().shoot();
        factory.createCar().driving();

    }
 }

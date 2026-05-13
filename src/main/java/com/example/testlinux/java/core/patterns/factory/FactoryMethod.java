package com.example.testlinux.java.core.patterns.factory;

public class FactoryMethod {
    public static void main(String[] stn) {
        factoryMethod();
        factorySimple();
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
 }

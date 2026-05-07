package com.example.testlinux.java.core.factory;

public class FactoryMethod {
    public static void main(String[] stn) {
        Logistics test = new RoadLogistics();
        test.deliver("test RoadLogistics");

        Logistics test2 = new SeaLogistics();
        test2.deliver("test SeaLogistics");
    }
}

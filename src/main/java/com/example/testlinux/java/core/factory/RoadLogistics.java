package com.example.testlinux.java.core.factory;

public class RoadLogistics extends Logistics {
    Transport createTransport() {
        System.out.println("createTransport Truck RoadLogistics");
        return new Truck();
    }
}
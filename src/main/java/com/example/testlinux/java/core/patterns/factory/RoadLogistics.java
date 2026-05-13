package com.example.testlinux.java.core.patterns.factory;

public class RoadLogistics extends Logistics {

    public RoadLogistics(String region) {
        super(region);
    }

    Transport createTransport() {
        System.out.println("createTransport Truck RoadLogistics");
        return new Truck();
    }
}
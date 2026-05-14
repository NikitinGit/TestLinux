package com.example.testlinux.java.core.patterns.factory.method;

public class RoadLogistics extends Logistics {

    public RoadLogistics(String region) {
        super(region);
    }

    public Transport createTransport() {
        System.out.println("createTransport Truck RoadLogistics");
        return new Truck();
    }
}
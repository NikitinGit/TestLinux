package com.example.testlinux.java.core.patterns.factory;

public class TransportFactory {

    public Transport create(TransportType type) {
        return switch (type) {
            case CAR -> new Car();
            case SHIP -> new Ship();
            case TRUCK -> new Truck();

            default -> throw new IllegalArgumentException("My custom error message!");
        };
    }
}

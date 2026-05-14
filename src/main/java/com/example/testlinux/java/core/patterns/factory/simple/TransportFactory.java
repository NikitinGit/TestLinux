package com.example.testlinux.java.core.patterns.factory.simple;

import com.example.testlinux.java.core.patterns.factory.TransportType;
import com.example.testlinux.java.core.patterns.factory.method.Car;
import com.example.testlinux.java.core.patterns.factory.method.Ship;
import com.example.testlinux.java.core.patterns.factory.method.Transport;
import com.example.testlinux.java.core.patterns.factory.method.Truck;

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

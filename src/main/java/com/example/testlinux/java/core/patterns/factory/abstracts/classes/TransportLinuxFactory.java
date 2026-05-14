package com.example.testlinux.java.core.patterns.factory.abstracts.classes;

import com.example.testlinux.java.core.patterns.factory.abstracts.interfaces.Car;
import com.example.testlinux.java.core.patterns.factory.abstracts.interfaces.Ship;
import com.example.testlinux.java.core.patterns.factory.abstracts.interfaces.TransportOSFactory;
import com.example.testlinux.java.core.patterns.factory.abstracts.interfaces.Truck;

public class TransportLinuxFactory implements TransportOSFactory {
    @Override
    public Ship createShip() {
        return new ShipLinux();
    }

    @Override
    public Truck createTruck() {
        return new TruckLinux();
    }

    @Override
    public Car createCar() {
        return new CarLinux();
    }
}

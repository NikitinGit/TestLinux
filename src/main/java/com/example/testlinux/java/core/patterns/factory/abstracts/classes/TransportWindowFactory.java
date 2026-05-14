package com.example.testlinux.java.core.patterns.factory.abstracts.classes;

import com.example.testlinux.java.core.patterns.factory.abstracts.interfaces.Car;
import com.example.testlinux.java.core.patterns.factory.abstracts.interfaces.Ship;
import com.example.testlinux.java.core.patterns.factory.abstracts.interfaces.TransportOSFactory;
import com.example.testlinux.java.core.patterns.factory.abstracts.interfaces.Truck;

public class TransportWindowFactory implements TransportOSFactory {

    @Override
    public Ship createShip() {
        return new ShipWindow();
    }

    @Override
    public Truck createTruck() {
        return new TruckWindow();
    }

    @Override
    public Car createCar() {
        return new CarWindow();
    }
}

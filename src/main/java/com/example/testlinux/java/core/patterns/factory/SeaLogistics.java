package com.example.testlinux.java.core.patterns.factory;

public class SeaLogistics extends Logistics {

    public SeaLogistics(String region) {
        super(region);
    }

    Transport createTransport() {
        System.out.println("createTransport Ship SeaLogistics");
        return new Ship();
    }
}

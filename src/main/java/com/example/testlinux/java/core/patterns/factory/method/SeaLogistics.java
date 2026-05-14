package com.example.testlinux.java.core.patterns.factory.method;

public class SeaLogistics extends Logistics {

    public SeaLogistics(String region) {
        super(region);
    }

    public Transport createTransport() {
        System.out.println("createTransport Ship SeaLogistics");
        return new Ship();
    }
}

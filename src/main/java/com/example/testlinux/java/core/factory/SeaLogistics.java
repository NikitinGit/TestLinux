package com.example.testlinux.java.core.factory;

public class SeaLogistics extends Logistics {
    Transport createTransport() {
        System.out.println("createTransport Ship SeaLogistics");
        return new Ship();
    }
}

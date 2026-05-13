package com.example.testlinux.java.core.factory;

public class FactoryMethod {
    public static void main(String[] stn) {
        var test = new RoadLogistics("Москва");
        test.deliver("test RoadLogistics");

        var test2 = new SeaLogistics("Владивосток");
        test2.deliver("test SeaLogistics");
    }
}

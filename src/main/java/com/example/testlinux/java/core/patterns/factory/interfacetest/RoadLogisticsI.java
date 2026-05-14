package com.example.testlinux.java.core.patterns.factory.interfacetest;

import com.example.testlinux.java.core.patterns.factory.method.Transport;
import com.example.testlinux.java.core.patterns.factory.method.Truck;

public class RoadLogisticsI implements LogisticsInterface {

    // Чтобы хранить region — приходится добавлять поле В САМОМ ПОДКЛАССЕ
    // Т.е. каждый подкласс (RoadLogisticsI, SeaLogisticsI, AirLogisticsI...)
    // вынужден копировать это поле и конструктор — дублирование кода
    private final String region;

    public RoadLogisticsI(String region) {
        this.region = region;
    }

    @Override
    public Transport createTransport() {
        System.out.println("createTransport Truck, region: " + region);
        return new Truck();
    }

    // И метод deliver() тоже придётся переопределить, чтобы использовать region —
    // иначе default-метод интерфейса про это поле ничего не знает
    @Override
    public void deliver(String name) {
        System.out.println("Region: " + region);  // дублируем логику из default-метода
        Transport t = createTransport();
        t.move();
        System.out.println("Delivered!");
    }
}
package com.example.testlinux.bean.order.life.circle;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class MyBean1 {

    public MyBean1() {
        System.out.println("➡️ MyBean1: constructor");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("➡️ MyBean1: @PostConstruct");
    }
}

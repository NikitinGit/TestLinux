package com.example.testlinux.configuration.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class BeanConfig {

    @Bean
    public A a() {
        System.out.println("CREATE A → " + System.identityHashCode(this));
        return new A();
    }

    @Bean
    public B b() {
        System.out.println("CALL b() → calling a()");
        B b = new B(a()); // <-- ВАЖНО: прямой вызов метода - без @Configuration создаст новый объект B
        return b;
    }
}


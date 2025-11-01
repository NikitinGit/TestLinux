package com.example.testlinux.test.primary;

import org.springframework.stereotype.Service;

@Service
public class RussianGreetingService implements GreetingService {

    @Override
    public String greet(String name) {
        return "Привет, " + name + "!";
    }
}

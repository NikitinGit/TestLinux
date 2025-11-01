package com.example.testlinux.test.primary;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary // ✅ Этот бин будет использоваться по умолчанию
public class EnglishGreetingService implements GreetingService {

    private  int count;
    @Override
    public String greet(String name) {
        return "Hello, " + name + "! - " + (++count);
    }
}

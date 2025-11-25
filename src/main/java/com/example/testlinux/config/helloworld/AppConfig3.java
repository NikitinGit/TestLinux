package com.example.testlinux.config.helloworld;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig3 {

    @Bean(name = "bean1")
    public MyBean myBean() {
        return new MyBean(852);
    }
}


package com.example.testlinux.config.helloworld;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig2 {

    @Bean(name = "bean2")
    public MyBean myBean() {
        return new MyBean(625);
    }
}

package com.example.testlinux.config.helloworld;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
        //@ComponentScan("com.example.testlinux.config")
public class AppConfig1 {

   // @Primary
    @Bean(name = "bean1")
    public MyBean myBean() {
        return new MyBean(25);
    }
}

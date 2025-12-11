package com.example.testlinux.bean.order.life.circle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanTest {

    int number = 25;

    @Bean("getBeanTest")
    public BeanTest getBeanTest(){
        System.out.println("@Bean Nikitin method CALLED");
        return new BeanTest();
    }

/*    @Override
    public String toString() {
        return "Nikitin; " + number;
    }*/
}

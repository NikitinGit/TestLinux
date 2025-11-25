package com.example.testlinux.bean.order.life.circle;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class MyBean implements InitializingBean {

    public MyBean() {
        System.out.println("➡️ MyBean: constructor");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("➡️ MyBean: @PostConstruct");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("➡️ MyBean: InitializingBean.afterPropertiesSet");
    }
}
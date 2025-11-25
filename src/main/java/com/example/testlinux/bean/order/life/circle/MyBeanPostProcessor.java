package com.example.testlinux.bean.order.life.circle;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {

            System.out.println("➡️ MyBeanPostProcessor: BEFORE initialization of " + beanName);

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {

            System.out.println("➡️ MyBeanPostProcessor: AFTER initialization of " + beanName);

        return bean;
    }
}

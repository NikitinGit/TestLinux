package com.example.testlinux;

import com.example.testlinux.config.helloworld.AppConfigOrderLifeCircle;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestBeanOrderLifeCircle {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfigOrderLifeCircle.class);

        String[] beanNames = context.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }

    }
}

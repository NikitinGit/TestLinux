package com.example.testlinux;

import com.example.testlinux.config.AppConfig1;
import com.example.testlinux.config.AppConfig2;
import com.example.testlinux.config.MyBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestBean {

    public static void main(String[] tons) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig1.class);
        MyBean bean1 = (MyBean) context.getBean("bean1");
        bean1.sayHello();

        MyBean bean2 = (MyBean) context.getBean("bean2");
        bean2.sayHello();

        // Вывести все имена бинов
        System.out.println("\n=== Все бины в контексте ===");
        String[] beanNames = context.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }
}

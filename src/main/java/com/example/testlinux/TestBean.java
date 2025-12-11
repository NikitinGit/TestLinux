package com.example.testlinux;

import com.example.testlinux.bean.order.life.circle.BeanTest;
import com.example.testlinux.config.helloworld.AppConfig1;
import com.example.testlinux.config.helloworld.AppConfig3;
import com.example.testlinux.config.helloworld.MyBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestBean {

    public static void main(String[] tons) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig1.class);
        MyBean bean1 = (MyBean) context.getBean("bean1");
        bean1.sayHello();

        ApplicationContext context3 = new AnnotationConfigApplicationContext(AppConfig3.class);
        MyBean bean2 = (MyBean) context3.getBean("bean1");
        bean2.sayHello();

        // Вывести все имена бинов
        System.out.println("\n=== Все бины в контексте ===");
        String[] beanNames = context.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }

        ApplicationContext contextCustomBean = new AnnotationConfigApplicationContext(BeanTest.class);
        BeanTest beanTest1 = (BeanTest) contextCustomBean.getBean("getBeanTest");
        System.out.println("beanTest1; " + beanTest1 + ", hash; " + beanTest1.hashCode());

        BeanTest beanTest2 = (BeanTest) contextCustomBean.getBean("getBeanTest");
        System.out.println("beanTest2; " + beanTest2 + ", hash; " + beanTest2.hashCode());

        String[] beanTestNames = contextCustomBean.getBeanDefinitionNames();
        for (String beanName : beanTestNames) {
            System.out.println(beanName);
        }

    }
}

package com.example.testlinux;

import com.example.testlinux.configuration.test.A;
import com.example.testlinux.configuration.test.B;
import com.example.testlinux.configuration.test.BeanConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestConfiguration {

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(BeanConfig.class);

        A a = ctx.getBean(A.class);
        B b = ctx.getBean(B.class);

        System.out.println("CTX A → " + a.hashCode());
    }
}

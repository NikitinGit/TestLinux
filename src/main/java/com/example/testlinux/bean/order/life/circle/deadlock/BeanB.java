package com.example.testlinux.bean.order.life.circle.deadlock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeanB {
    /*public BeanB(BeanA beanA) {
        System.out.println("BeanB(BeanA beanA)");
    }*/
    @Autowired
    private BeanA beanA;
}

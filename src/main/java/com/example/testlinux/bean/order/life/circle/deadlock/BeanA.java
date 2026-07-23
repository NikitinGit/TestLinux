package com.example.testlinux.bean.order.life.circle.deadlock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class BeanA {
    /*public BeanA(BeanB beanB) {
        System.out.println("BeanA(BeanB beanB)");
    } // ТУПИК: Spring не может вызвать этот конструктор*/
    @Autowired
    @Lazy//без него впадет ошибка в циклической зависимости
    private BeanB beanB; // Внедрение через поле
}

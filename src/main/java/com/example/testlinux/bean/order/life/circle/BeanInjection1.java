package com.example.testlinux.bean.order.life.circle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeanInjection1 {

    private MyBean myBean;

    @Autowired
    public BeanInjection1(MyBean myBean) {
        System.out.println("➡️ DI (BeanInjection1): myBean injected");
        this.myBean = myBean;
    }
}

package com.example.testlinux.bean.order.life.circle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeanInjection {

    private MyBean myBean;

    @Autowired
    public void setDependency(MyBean myBean) {
        System.out.println("➡️ DI (Setter): myBean injected");
        this.myBean = myBean;
    }
}

package com.example.testlinux.config.helloworld;

import org.springframework.stereotype.Component;

import static java.lang.System.out;

@Component
public class ComponentObjBean {

    public ComponentObjBean() {
        out.println("ComponentObjBean() ");
    }
}

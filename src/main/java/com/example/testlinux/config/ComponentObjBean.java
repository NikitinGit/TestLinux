package com.example.testlinux.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static java.lang.System.out;

@Component
public class ComponentObjBean {

    public ComponentObjBean() {
        out.println("ComponentObjBean() ");
    }
}

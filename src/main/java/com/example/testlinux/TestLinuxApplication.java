package com.example.testlinux;

import com.example.testlinux.config.SpringConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class TestLinuxApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestLinuxApplication.class, args);
        System.out.println("Hello, Linux!");
    }

}

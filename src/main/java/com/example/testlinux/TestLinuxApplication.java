package com.example.testlinux;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestLinuxApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestLinuxApplication.class, args);
        System.out.println("Hello, Linux 25!");
    }

}

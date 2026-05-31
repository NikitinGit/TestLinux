package com.example.testlinux;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
        basePackages = "com.example.testlinux",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = "com\\.example\\.testlinux\\.config\\.autoconf\\..*"
        )
)
public class TestLinuxApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestLinuxApplication.class, args);
        System.out.println("Hello, Linux 25!");
    }

}

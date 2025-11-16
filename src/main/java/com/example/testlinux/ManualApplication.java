package com.example.testlinux;


import com.example.testlinux.config.autoconf.JpaConfig;
import com.example.testlinux.config.autoconf.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("com.example.testlinux")
@EnableJpaRepositories(basePackages = "com.example.testlinux.repository")
@Import({JpaConfig.class, WebConfig.class})
public class ManualApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        // Запуск Spring Boot приложения с веб-сервером
        SpringApplication app = new SpringApplication(ManualApplication.class);
        ConfigurableApplicationContext context = app.run(args);

        System.out.println("Hello, Linux 25!");

        // Вывод всех бинов
        System.out.println("\n=== Все бины в контексте ===");
        String[] beanNames = context.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }
}

package com.example.testlinux.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

//@Configuration
//@ComponentScan("com.example.testlinux")
//@PropertySource("classpath:application.properties")
public class SpringConfig {
    /*@Autowired
    Environment environment;

    private final String URL = "spring.datasource.url";
    private final String USER = "spring.datasource.username";
    private final String DRIVER = "spring.datasource.driver-class-name";
    private final String PASSWORD = "spring.datasource.password";

    @Bean
    DataSource dataSource() {

        System.out.println(URL + "=" + environment.getProperty(URL));
        System.out.println(USER + "=" + environment.getProperty(USER));
        System.out.println(DRIVER + "=" + environment.getProperty(DRIVER));
        System.out.println(PASSWORD + "=" + environment.getProperty(PASSWORD));

        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl(environment.getProperty(URL));
        driverManagerDataSource.setUsername(environment.getProperty(USER));
        driverManagerDataSource.setPassword(environment.getProperty(PASSWORD));
        driverManagerDataSource.setDriverClassName(environment.getProperty(DRIVER));
        return driverManagerDataSource;
    }*/
}

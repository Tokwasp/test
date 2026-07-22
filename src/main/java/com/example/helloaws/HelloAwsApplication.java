package com.example.helloaws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class HelloAwsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloAwsApplication.class, args);
    }
}

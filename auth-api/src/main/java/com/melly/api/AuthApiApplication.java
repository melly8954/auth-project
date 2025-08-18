package com.melly.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.melly"})
@EnableJpaRepositories(basePackages = "com.melly.domain.repository")
@EntityScan(basePackages = "com.melly.domain.entity")
public class AuthApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApiApplication.class, args);
    }
}

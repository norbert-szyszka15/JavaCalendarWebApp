package org.example.javacalendarwebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("org.example.javacalendarwebapp.entity")
@EnableJpaRepositories("org.example.javacalendarwebapp.repository")
public class JavaCalendarWebAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(JavaCalendarWebAppApplication.class, args);
    }
}

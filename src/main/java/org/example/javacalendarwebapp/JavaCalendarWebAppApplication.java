package org.example.javacalendarwebapp;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
  info = @Info(
    title       = "Java Calendar Web App API",
    version     = "v1.0",
    description = "REST API dla aplikacji kalendarza",
    contact     = @Contact(name = "Zespół Dev", email = "dev@example.com")
  )
)
public class JavaCalendarWebAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(JavaCalendarWebAppApplication.class, args);
    }
}
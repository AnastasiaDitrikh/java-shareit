package ru.practicum.shareit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *  Запускает приложение(микросервис) ShareIt Gateway с использованием Spring Boot.
 */
@SpringBootApplication
public class ShareItGateway {
    public static void main(String[] args) {
        SpringApplication.run(ShareItGateway.class, args);
    }
}
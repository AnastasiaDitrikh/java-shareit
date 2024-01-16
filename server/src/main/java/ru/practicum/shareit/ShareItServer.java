package ru.practicum.shareit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Класс ShareItServer, представляющий сервер приложения ShareIt.
 */
@SpringBootApplication
public class ShareItServer {

    /**
     * Метод main, запускающий сервер приложения ShareIt.
     */
    public static void main(String[] args) {
        SpringApplication.run(ShareItServer.class, args);
    }
}
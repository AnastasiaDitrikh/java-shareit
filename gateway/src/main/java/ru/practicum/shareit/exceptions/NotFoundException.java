package ru.practicum.shareit.exceptions;

/**
 * Класс исключения NotFoundException, представляющий ошибку, когда запрашиваемый ресурс не найден.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

}
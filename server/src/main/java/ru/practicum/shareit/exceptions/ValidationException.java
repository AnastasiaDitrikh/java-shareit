package ru.practicum.shareit.exceptions;

/**
 * Класс исключения ValidationException, представляющий ошибку валидации данных.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
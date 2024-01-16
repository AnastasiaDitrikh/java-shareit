package ru.practicum.shareit.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Класс, представляющий объект ответа на ошибку.
 */
@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final String error;
}
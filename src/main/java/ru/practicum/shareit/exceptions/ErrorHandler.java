package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.warn("Получен статус 404 NOT_FOUND {}", e.getMessage(), e);
        return new ErrorResponse(
                e.getMessage()

        );
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final Exception e) {
        log.warn("Получен статус 400 BAD_REQUEST {}", e.getMessage(), e);
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler({NotUniqueEmailException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleNotUniqueEmailException(final RuntimeException e) {
        log.warn("Не уникальный Email.");
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOtherException(final Throwable e) {
        log.warn("Получен статус 500 SERVER_ERROR {}", e.getMessage(), e);
        return new ErrorResponse(
                e.getMessage()
        );
    }
}
package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Класс, отвечающий за обработку ошибок и генерацию соответствующих ответов.
 */
@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    /**
     * Обработчик исключения NotFoundException.
     *
     * @param e Исключение NotFoundException.
     * @return Объект ErrorResponse с сообщением об ошибке.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.warn("Получен статус 404 NOT_FOUND {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Обработчик исключений MethodArgumentNotValidException или ValidationException
     *
     * @param e Исключение MethodArgumentNotValidException или ValidationException
     * @return Объект ErrorResponse с сообщением об ошибке.
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final Exception e) {
        log.warn("Получен статус 400 BAD_REQUEST {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Обработчик других исключений.
     *
     * @param e Исключение.
     * @return Объект ErrorResponse с сообщением об ошибке.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOtherException(final Throwable e) {
        log.warn("Получен статус 500 SERVER_ERROR {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }
}
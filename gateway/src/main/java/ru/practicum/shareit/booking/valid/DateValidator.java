package ru.practicum.shareit.booking.valid;

import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

/**
 * Класс DateValidator является реализацией ConstraintValidator для проверки правильности даты начала и даты окончания
 * в объекте BookItemRequestDto.
 * Проверяет, что дата начала находится перед датой окончания.
 */
public class DateValidator implements ConstraintValidator<StartBeforeEndDateValid, BookItemRequestDto> {

    /**
     * Метод initialize инициализирует проверку.
     *
     * @param constraintAnnotation аннотация, объявленная над проверяемым полем класса
     */
    @Override
    public void initialize(StartBeforeEndDateValid constraintAnnotation) {
    }

    /**
     * Метод isValid выполняет саму проверку, сравнивая дату начала с датой окончания.
     *
     * @param bookItemRequestDto             объект BookItemRequestDto для проверки
     * @param constraintValidatorContext контекст проверки, предоставляемый конкретной реализацией
     * @return true, если дата начала находится перед датой окончания, иначе false
     */
    @Override
    public boolean isValid(BookItemRequestDto bookItemRequestDto, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = bookItemRequestDto.getStart();
        LocalDateTime end = bookItemRequestDto.getEnd();
        if (start == null || end == null) {
            return false;
        }
        return start.isBefore(end);
    }
}
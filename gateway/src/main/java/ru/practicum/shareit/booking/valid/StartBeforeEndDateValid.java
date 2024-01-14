package ru.practicum.shareit.booking.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Аннотация StartBeforeEndDateValid является пользовательской аннотацией для проверки правильности даты начала и даты окончания.
 * Аннотация используется в комбинации с валидатором DateValidator.
 * Если дата начала находится перед датой окончания или обе даты не равны null, проверка считается успешной.
 */
@Target(ElementType.TYPE_USE)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = DateValidator.class)
public @interface StartBeforeEndDateValid {

    /**
     * Метод message возвращает сообщение, которое будет возвращено в случае несоответствия условия проверки.
     *
     * @return сообщение о нарушении условия проверки
     */
    String message() default "Start must be before end or not null";

    /**
     * Метод groups возвращает группы, к которым принадлежит аннотация.
     *
     * @return массив классов групп
     */
    Class<?>[] groups() default {};

    /**
     * Метод payload возвращает пустой массив классов, необходимый для расширяемости аннотации.
     *
     * @return массив классов Payload
     */
    Class<? extends Payload>[] payload() default {};
}
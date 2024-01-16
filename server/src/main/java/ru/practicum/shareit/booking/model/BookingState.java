package ru.practicum.shareit.booking.model;

import java.util.Arrays;

/**
 * Перечисление BookingState представляет возможные состояния бронирования.
 * Включает в себя следующие состояния:
 * - ALL: все состояния
 * - CURRENT: текущее состояние
 * - FUTURE: будущее состояние
 * - PAST: прошедшее состояние
 * - REJECTED: отклоненное состояние
 * - WAITING: ожидающее подтверждение состояние
 */
public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    /**
     * Метод from(String bookingState) выполняет преобразование строки состояния бронирования в объект BookingState.
     *
     * @param bookingState Строка состояния бронирования
     * @return Возвращает объект BookingState, соответствующий переданной строке состояния бронирования.
     * Если соответствующего значения не найдено, возвращает null
     */
    public static BookingState from(String bookingState) {
        return Arrays.stream(BookingState.values())
                .filter(value -> value.name().equals(bookingState))
                .findFirst()
                .orElse(null);
    }
}
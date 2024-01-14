package ru.practicum.shareit.booking.dto;

import java.util.Optional;

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
    FUTURE,
    PAST,
    REJECTED,
    WAITING;

    /**
     * Метод from возвращает объект BookingState, соответствующий указанной строке состояния.
     *
     * @param stringState строка состояния
     * @return объект Optional, содержащий BookingState, или пустой, если состояние не найдено
     */
    public static Optional<BookingState> from(String stringState) {
        for (BookingState state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
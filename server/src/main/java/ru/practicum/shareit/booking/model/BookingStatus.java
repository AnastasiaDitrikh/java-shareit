package ru.practicum.shareit.booking.model;

/**
 * Перечисление BookingStatus, представляющее возможные статусы бронирования.
 * Возможные значения:
 * - WAITING: Ожидание подтверждения бронирования.
 * - APPROVED: Бронирование подтверждено.
 * - REJECTED: Бронирование отклонено.
 * - CANCELED: Бронирование отменено.
 */
public enum BookingStatus {
    WAITING,
    APPROVED,
    REJECTED,
    CANCELED
}
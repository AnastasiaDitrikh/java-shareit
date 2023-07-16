package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.List;


public interface BookingService {
    BookingDtoOut add(Long userId, BookingDto bookingDto);

    BookingDtoOut update(Long userId, Long bookingId, Boolean approved);

    BookingDtoOut findBookingByUserId(Long userId, Long bookingId);

    List<BookingDtoOut> findAll(Long userId, String state);

    List<BookingDtoOut> findAllOwner(Long userId, String state);
}
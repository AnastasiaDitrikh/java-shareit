package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;


public class BookingMapper {
    public static Booking toBooking(User user, Item item, BookingDto bookingDto) {
        return new Booking(
                item,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                user,
                BookingStatus.WAITING);
    }

    public static BookingDtoOut toBookingOut(Booking booking) {
        return new BookingDtoOut(
                booking.getId(),
                booking.getItem(),
                booking.getStart(),
                booking.getEnd(),
                booking.getBooker(),
                booking.getStatus());
    }

    public static BookingItemDto toBookingItemDto(Booking booking) {
        return new BookingItemDto(
                booking.getId(),
                booking.getBooker().getId());
    }
}
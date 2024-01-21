package ru.practicum.shareit.booking;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;

/**
 * Класс BookingMapper, представляющий маппер для преобразования объектов Booking и связанных классов.
 */
@UtilityClass
public class BookingMapper {

    /**
     * Преобразует объект BookingDto в объект Booking.
     *
     * @param user       Объект User, представляющий пользователя, совершающего бронирование.
     * @param item       Объект Item, представляющий брон
     * @param bookingDto Объект BookingDto, содержащий данные бронирования.
     * @return Объект Booking, представляющий бронирование.
     */
    public Booking toBooking(User user, Item item, BookingDto bookingDto) {
        return Booking.builder()
                .item(item)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();
    }

    /**
     * Преобразует объект бронирования (Booking) в объект BookingDtoOut.
     *
     * @param booking Объект бронирования, который требуется преобразовать.
     * @return Объект BookingDtoOut, содержащий данные о бронировании.
     */
    public BookingDtoOut toBookingOut(Booking booking) {
        return BookingDtoOut.builder()
                .id(booking.getId())
                .item(ItemMapper.toItemDtoOut(booking.getItem()))
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(UserMapper.toUserDto(booking.getBooker()))
                .status(booking.getStatus())
                .build();
    }

    /**
     * Преобразует объект бронирования (Booking) в объект BookingItemDto.
     *
     * @param booking Объект бронирования, который требуется преобразовать.
     * @return Объект BookingItemDto, содержащий данные об идентификаторе бронирования и идентификаторе бронирующего пользователя.
     */
    public static BookingItemDto toBookingItemDto(Booking booking) {
        return BookingItemDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }
}
package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingServiceIT {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    private final UserDto userDto1 = UserDto.builder()
            .name("name1")
            .email("email1@email.com")
            .build();

    private final UserDto userDto2 = UserDto.builder()
            .name("name2")
            .email("email2@email.com")
            .build();

    private final ItemDto itemDto1 = ItemDto.builder()
            .name("item1 name")
            .description("item1 description")
            .available(true)
            .build();

    private final ItemDto itemDto2 = ItemDto.builder()
            .name("item2 name")
            .description("item2 description")
            .available(true)
            .build();

    private final BookingDto bookingDto1 = BookingDto.builder()
            .itemId(2L)
            .start(LocalDateTime.now().plusSeconds(10L))
            .end(LocalDateTime.now().plusSeconds(11L))
            .build();

    @Test
    void addBooking() {
        UserDto addedUser1 = userService.add(userDto1);
        UserDto addedUser2 = userService.add(userDto2);
        itemService.add(addedUser1.getId(), itemDto1);
        itemService.add(addedUser2.getId(), itemDto2);

        BookingDtoOut bookingDtoOut1 = bookingService.add(addedUser1.getId(), bookingDto1);
        BookingDtoOut bookingDtoOut2 = bookingService.add(addedUser1.getId(), bookingDto1);

        assertEquals(1L, bookingDtoOut1.getId());
        assertEquals(2L, bookingDtoOut2.getId());
        assertEquals(BookingStatus.WAITING, bookingDtoOut1.getStatus());
        assertEquals(BookingStatus.WAITING, bookingDtoOut2.getStatus());

        BookingDtoOut updatedBookingDto1 = bookingService.update(addedUser2.getId(),
                bookingDtoOut1.getId(), true);
        BookingDtoOut updatedBookingDto2 = bookingService.update(addedUser2.getId(),
                bookingDtoOut2.getId(), true);

        assertEquals(BookingStatus.APPROVED, updatedBookingDto1.getStatus());
        assertEquals(BookingStatus.APPROVED, updatedBookingDto2.getStatus());

        List<BookingDtoOut> bookingsDtoOut = bookingService.findAllOwner(addedUser2.getId(),
                BookingState.ALL.toString(), 0, 10);

        assertEquals(2, bookingsDtoOut.size());
    }

    @Test
    void update_whenBookingIdAndUserIdIsNotValid_thenThrowObjectNotFoundException() {
        Long userId = 3L;
        Long bookingId = 3L;

        Assertions
                .assertThrows(NotFoundException.class,
                        () -> bookingService.update(userId, bookingId, true));
    }
}
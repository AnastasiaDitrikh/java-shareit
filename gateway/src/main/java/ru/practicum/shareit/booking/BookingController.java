package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.user.markers.Create;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.Constants.USER_HEADER;

/**
 * Класс BookingController является контроллером для обработки запросов, связанных с бронированием.
 * Класс содержит методы для выполнения операций на получение, создание, обновление и удаление бронирования.
 */
@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    /**
     * Метод getBookings выполняет запрос на получение списка бронирований.
     *
     * @param userId     ID пользователя
     * @param stateParam состояние бронирования
     * @param from       -
     * @param size       размер
     * @return объект ResponseEntity с результатом запроса
     */
    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader(USER_HEADER) long userId,
                                              @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam).orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, state, from, size);
    }

    /**
     * Метод bookItem выполняет запрос на создание бронирования.
     *
     * @param userId     ID пользователя
     * @param requestDto объект BookItemRequestDto
     * @return объект ResponseEntity с результатом запроса
     */
    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader(USER_HEADER) long userId,
                                           @Validated(Create.class) @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    /**
     * Метод getBooking выполняет запрос на получение бронирования по его ID.
     *
     * @param userId    ID пользователя
     * @param bookingId ID бронирования
     * @return объект ResponseEntity с результатом запроса
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(USER_HEADER) long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    /**
     * Метод getAllOwner выполняет запрос на получение списка бронирований по владельцу вещи
     *
     * @param ownerId      ID владельца
     * @param bookingState состояние бронирования
     * @param from         -
     * @param size         размер
     * @return объект ResponseEntity с результатом запроса
     */
    @GetMapping("/owner")
    public ResponseEntity<Object> getAllOwner(@RequestHeader(USER_HEADER) Long ownerId,
                                              @RequestParam(value = "state", defaultValue = "ALL") String bookingState,
                                              @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                              @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        BookingState state = BookingState.from(bookingState)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + bookingState));
        log.info("GET запрос на получение списка всех бронирований c state {}, userId={}, from={}, size={}", bookingState, ownerId, from, size);
        return bookingClient.getAllOwner(ownerId, state, from, size);
    }

    /**
     * Метод updateStatus выполняет запрос на обновление статуса бронирования.
     *
     * @param userId    ID пользователя
     * @param bookingId ID бронирования
     * @param approved  флаг подтверждения
     * @return объект ResponseEntity с результатом запроса
     */
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateStatus(@RequestHeader(USER_HEADER) Long userId,
                                               @PathVariable("bookingId") Long bookingId,
                                               @RequestParam("approved") Boolean approved) {
        log.info("PATCH запрос на обновление статуса бронирования вещи : {} от владельца с id: {}", bookingId, userId);
        return bookingClient.update(userId, bookingId, approved);
    }
}
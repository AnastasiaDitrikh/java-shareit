package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

import static ru.practicum.shareit.item.ItemController.USER_HEADER;

/**
 * Класс BookingController обрабатывает HTTP-запросы, связанные с бронированием вещей.
 * Он предоставляет точки входа для создания, обновления и получения информации о бронированиях.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingService bookingService;

    /**
     * Обработчик POST-запроса для создания нового бронирования вещи.
     *
     * @param userId     Идентификатор пользователя, создающего бронирование (из заголовка запроса).
     * @param bookingDto DTO объект, содержащий данные о бронировании (из тела запроса).
     * @return DTO объект, содержащий данные о созданном бронировании.
     */
    @PostMapping
    public BookingDtoOut create(@RequestHeader(USER_HEADER) Long userId,
                                @RequestBody BookingDto bookingDto) {
        log.info("POST запрос на создание нового бронирования вещи от пользователя c id: {} ", userId);
        return bookingService.add(userId, bookingDto);
    }

    /**
     * Обработчик PATCH-запроса для обновления статуса бронирования вещи.
     *
     * @param userId     Идентификатор пользователя, выполняющего обновление (из заголовка запроса).
     * @param bookingId  Идентификатор бронирования, которое требуется обновить (из пути запроса).
     * @param approved   Флаг, указывающий на подтверждение или отклонение бронирования.
     * @return DTO объект, содержащий обновленные данные о бронировании.
     */
    @PatchMapping("/{bookingId}")
    public BookingDtoOut updateStatus(@RequestHeader(USER_HEADER) Long userId,
                                      @PathVariable("bookingId")
                                      Long bookingId,
                                      @RequestParam(name = "approved") Boolean approved) {
        log.info("PATCH запрос на обновление статуса бронирования вещи от владельца с id: {}", userId);
        return bookingService.update(userId, bookingId, approved);
    }

    /**
     * Обработчик GET-запроса для получения данных о бронировании по его идентификатору.
     *
     * @param userId     Идентификатор пользователя, выполняющего запрос (из заголовка запроса).
     * @param bookingId  Идентификатор бронирования, которое требуется получить (из пути запроса).
     * @return DTO объект, содержащий данные о бронировании.
     */
    @GetMapping("/{bookingId}")
    public BookingDtoOut findBookingById(@RequestHeader(USER_HEADER) Long userId,
                                         @PathVariable("bookingId")
                                         Long bookingId) {
        log.info("GET запрос на получение данных о  бронировании от пользователя с id: {}", userId);
        return bookingService.findBookingByUserId(userId, bookingId);
    }

    /**
     * Обработчик GET-запроса для получения списка всех бронирований пользователя.
     *
     * @param userId        Идентификатор пользователя, выполняющего запрос (из заголовка запроса).
     * @param bookingState  Статус бронирований для фильтрации (из параметра запроса).
     * @param from          Индекс начала списка для пагинации (из параметра запроса).
     * @param size          Размер списка для пагинации (из параметра запроса).
     * @return Список DTO объектов, содержащих данные о бронированиях пользователя.
     */
    @GetMapping
    public List<BookingDtoOut> findAll(@RequestHeader(USER_HEADER) Long userId,
                                       @RequestParam(value = "state", defaultValue = "ALL") String bookingState,
                                       @RequestParam(value = "from", defaultValue = "0") Integer from,
                                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("GET запрос на получение списка всех бронирований текущего пользователя с id: {} и статусом {}", userId, bookingState);
        return bookingService.findAll(userId, bookingState, from, size);
    }

    /**
     * Обработчик GET-запроса для получения списка всех бронирований владельца.
     *
     * @param ownerId       Идентификатор владельца, для которого выполняется запрос (из заголовка запроса).
     * @param bookingState  Статус бронирований для фильтрации (из параметра запроса).
     * @param from          Индекс начала списка для пагинации (из параметра запроса).
     * @param size          Размер списка для пагинации (из параметра запроса).
     * @return Список DTO объектов, содержащих данные о бронированиях владельца.
     */
    @GetMapping("/owner")
    public List<BookingDtoOut> getAllOwner(@RequestHeader(USER_HEADER) Long ownerId,
                                           @RequestParam(value = "state", defaultValue = "ALL") String bookingState,
                                           @RequestParam(value = "from", defaultValue = "0") Integer from,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("GET запрос на получение списка всех бронирований текущего владельца с id: {} и статусом {}", ownerId, bookingState);
        return bookingService.findAllOwner(ownerId, bookingState, from, size);
    }
}
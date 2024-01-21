package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Реализация интерфейса BookingService для обработки операций с бронированием.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;


    /**
     * Создает новое бронирование на основе переданных данных.
     *
     * @param userId     Идентификатор пользователя, создающего бронирование.
     * @param bookingDto DTO объект, содержащий данные о бронировании.
     * @return DTO объект, содержащий данные о созданном бронировании.
     * @throws NotFoundException если предмет не найден.
     */
    @Override
    @Transactional
    public BookingDtoOut add(Long userId, BookingDto bookingDto) {
        User user = UserMapper.toUser(userService.findById(userId));
        Optional<Item> itemById = itemRepository.findById(bookingDto.getItemId());

        if (itemById.isEmpty()) {
            throw new NotFoundException("Вещь не найдена.");
        }

        Item item = itemById.get();
        bookingValidation(bookingDto, user, item);
        Booking booking = BookingMapper.toBooking(user, item, bookingDto);
        return BookingMapper.toBookingOut(bookingRepository.save(booking));
    }

    /**
     * Обновляет статус бронирования на основе переданных данных.
     *
     * @param userId    Идентификатор пользователя, выполняющего обновление.
     * @param bookingId Идентификатор бронирования, которое требуется обновить.
     * @param approved  Флаг, указывающий на одобрение или отклонение бронирования.
     * @return DTO объект, содержащий обновленные данные о бронировании.
     */
    @Override
    @Transactional
    public BookingDtoOut update(Long userId, Long bookingId, Boolean approved) {
        Booking booking = validateBookingDetails(userId, bookingId, 1);
        assert booking != null;
        BookingStatus newStatus = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        booking.setStatus(newStatus);
        return BookingMapper.toBookingOut(bookingRepository.save(booking));
    }

    /**
     * Возвращает бронирование по его идентификатору и идентификатору пользователя.
     *
     * @param userId    Идентификатор пользователя, выполняющего запрос.
     * @param bookingId Идентификатор бронирования, которое требуется получить.
     * @return DTO объект, содержащий данные о бронировании.
     */
    @Override
    @Transactional
    public BookingDtoOut findBookingByUserId(Long userId, Long bookingId) {
        Booking booking = validateBookingDetails(userId, bookingId, 2);
        assert booking != null;
        return BookingMapper.toBookingOut(booking);
    }

    /**
     * Возвращает список всех бронирований пользователя или владельца,
     * в зависимости от переданного идентификатора.
     *
     * @param bookerId Идентификатор пользователя или владельца.
     * @param bookingState    Статус бронирований для фильтрации.
     * @param from     Индекс начала списка для пагинации.
     * @param size     Размер списка для пагинации.
     * @return Список DTO объектов, содержащих данные о бронированиях.
     */
    @Override
    @Transactional
    public List<BookingDtoOut> findAll(Long bookerId, String bookingState, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        userService.findById(bookerId);
        switch (validState(bookingState)) {
            case ALL:
                return bookingRepository.findAllBookingsByBookerId(bookerId, pageable).stream()
                        .map(BookingMapper::toBookingOut)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findAllCurrentBookingsByBookerId(bookerId, LocalDateTime.now(), pageable).stream()
                        .map(BookingMapper::toBookingOut)
                        .collect(Collectors.toList());

            case PAST:
                return bookingRepository.findAllPastBookingsByBookerId(bookerId, LocalDateTime.now(), pageable).stream()
                        .map(BookingMapper::toBookingOut)
                        .collect(Collectors.toList());

            case FUTURE:
                return bookingRepository.findAllFutureBookingsByBookerId(bookerId, LocalDateTime.now(), pageable).stream()
                        .map(BookingMapper::toBookingOut)
                        .collect(Collectors.toList());

            case WAITING:
                return bookingRepository.findAllWaitingBookingsByBookerId(bookerId, LocalDateTime.now(), pageable).stream()
                        .map(BookingMapper::toBookingOut)
                        .collect(Collectors.toList());

            case REJECTED:
                return bookingRepository.findAllRejectedBookingsByBookerId(bookerId, LocalDateTime.now(), pageable).stream()
                        .map(BookingMapper::toBookingOut)
                        .collect(Collectors.toList());
            default:
                throw new IllegalArgumentException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    /**
     * Возвращает список всех бронирований владельца с заданным состоянием.
     *
     * @param ownerId         Идентификатор владельца.
     * @param bookingState    Статус бронирований для фильтрации.
     * @param from            Индекс начала списка для пагинации.
     * @param size            Размер списка для пагинации.
     * @return Список DTO объектов, содержащих данные о бронированиях.
     */
    @Override
    @Transactional
    public List<BookingDtoOut> findAllOwner(Long ownerId, String bookingState, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        userService.findById(ownerId);
        switch (validState(bookingState)) {
            case ALL:
                return bookingRepository.findAllBookingsByOwnerId(ownerId, pageable).stream()
                        .map(BookingMapper::toBookingOut)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findAllCurrentBookingsByOwnerId(ownerId, LocalDateTime.now(), pageable).stream()
                        .map(BookingMapper::toBookingOut)
                        .collect(Collectors.toList());

            case PAST:
                return bookingRepository.findAllPastBookingsByOwnerId(ownerId, LocalDateTime.now(), pageable).stream()
                        .map(BookingMapper::toBookingOut)
                        .collect(Collectors.toList());

            case FUTURE:
                return bookingRepository.findAllFutureBookingsByOwnerId(ownerId, LocalDateTime.now(), pageable).stream()
                        .map(BookingMapper::toBookingOut)
                        .collect(Collectors.toList());

            case WAITING:
                return bookingRepository.findAllWaitingBookingsByOwnerId(ownerId, LocalDateTime.now(), pageable).stream()
                        .map(BookingMapper::toBookingOut)
                        .collect(Collectors.toList());

            case REJECTED:
                return bookingRepository.findAllRejectedBookingsByOwnerId(ownerId, pageable).stream()
                        .map(BookingMapper::toBookingOut)
                        .collect(Collectors.toList());
            default:
                throw new IllegalArgumentException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    /**
     * Проверяет данные бронирования.
     *
     * @param bookingDto - объект с информацией о бронировании
     * @param user - объект пользователя
     * @param item - объект вещи
     * @throws ValidationException - если вещь не доступна для бронирования или дата окончания раньше или равна дате начала
     * @throws NotFoundException - если вещь не найдена
     */
    private void bookingValidation(BookingDto bookingDto, User user, Item item) {
        // Проверить доступность вещи для бронирования
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь не доступна для бронирования.");
        }

        // Проверить, что пользователь не является владельцем вещи
        if (user.getId().equals(item.getOwner().getId())) {
            throw new NotFoundException("Вещь не найдена.");
        }

        // Проверить, что дата окончания не раньше или равна дате начала
        if (bookingDto.getStart().isAfter(bookingDto.getEnd()) || bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            throw new ValidationException("Дата окончания не может быть раньше или равна дате начала");
        }
    }

    /**
     * Проверяет правильность состояния бронирования.
     *
     * @param bookingState - состояние бронирования
     * @return правильное состояние бронирования
     * @throws IllegalArgumentException - если состояние бронирования неизвестно
     */
    private BookingState validState(String bookingState) {
        BookingState state = BookingState.from(bookingState);
        if (state == null) {
            throw new IllegalArgumentException("Unknown state: " + bookingState);
        }
        return state;
    }


    /**
     * Проверяет информацию о бронировании.
     *
     * @param userId - ID пользователя
     * @param bookingId - ID бронирования
     * @param number - номер проверки (1 - для владельца или 2 - для владельца и автора бронирования)
     * @return объект бронирования
     * @throws NotFoundException - если бронь не найдена
     * @throws ValidationException - если бронь не со статусом WAITING
     */
    private Booking validateBookingDetails(Long userId, Long bookingId, Integer number) {
        // Получить бронирование по ID
        Optional<Booking> bookingById = bookingRepository.findById(bookingId);

        // Проверить, что бронь существует
        if (bookingById.isEmpty()) {
            throw new NotFoundException("Бронь не найдена.");
        }

        Booking booking = bookingById.get();

        // Проверить информацию о бронировании в зависимости от номера проверки
        switch (number) {
            case 1:
                // Проверить, что пользователь является владельцем вещи и бронь имеет статус WAITING
                if (!booking.getItem().getOwner().getId().equals(userId)) {
                    throw new NotFoundException("Пользователь не является владельцем");
                }
                if (!booking.getStatus().equals(BookingStatus.WAITING)) {
                    throw new ValidationException("Бронь не cо статусом WAITING");
                }
                return booking;
            case 2:
                // Проверить, что пользователь является владельцем вещи или автором бронирования
                if (!booking.getBooker().getId().equals(userId)
                        && !booking.getItem().getOwner().getId().equals(userId)) {
                    throw new NotFoundException("Пользователь не владелец и не автор бронирования ");
                }
                return booking;
        }

        return null;
    }
}
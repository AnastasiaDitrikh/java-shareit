package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final UserService userService;

    /**
     * Создает новую вещь на основе данных из объекта ItemDto и добавляет ее в базу данных.
     *
     * @param userId - ID пользователя
     * @param itemDto - объект данных вещи
     * @return объект ItemDtoOut созданной вещи
     * @throws NotFoundException - если пользователь или запрос не найдены
     */
    @Override
    @Transactional
    public ItemDtoOut add(Long userId, ItemDto itemDto) {
        UserDto user = userService.findById(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner((UserMapper.toUser(user)));

        if (itemDto.getRequestId() != null) {
            item.setRequest(itemRequestRepository.getReferenceById(itemDto.getRequestId()));
        }

        return ItemMapper.toItemDtoOut(itemRepository.save(item));
    }

    /**
     * Обновляет данные существующей вещи на основе данных из объекта ItemDto.
     *
     * @param userId - ID пользователя
     * @param itemId - ID вещи
     * @param itemDto - объект данных вещи
     * @return объект ItemDtoOut обновленной вещи
     * @throws NotFoundException - если вещь или пользователь не найдены
     */
    @Override
    @Transactional
    public ItemDtoOut update(Long userId, Long itemId, ItemDto itemDto) {
        UserDto user = userService.findById(userId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещи с " + itemId + " не существует"));

        if (!UserMapper.toUser(user).equals(item.getOwner())) {
            throw new NotFoundException("Пользователь с id = " + userId +
                    " не является собственником вещи id = " + itemId);
        }
        Boolean isAvailable = itemDto.getAvailable();
        if (isAvailable != null) {
            item.setAvailable(isAvailable);
        }
        String description = itemDto.getDescription();
        if (description != null && !description.isBlank()) {
            item.setDescription(description);
        }
        String name = itemDto.getName();
        if (name != null && !name.isBlank()) {
            item.setName(name);
        }

        return ItemMapper.toItemDtoOut(item);
    }

    /**
     * Находит вещь по ее ID и возвращает объект ItemDtoOut с информацией о ней.
     *
     * @param userId - ID пользователя
     * @param itemId - ID вещи
     * @return объект ItemDtoOut с информацией о вещи
     * @throws NotFoundException - если вещь или пользователь не найдены
     */
    @Override
    @Transactional
    public ItemDtoOut findItemById(Long userId, Long itemId) {
        userService.findById(userId);
        Optional<Item> itemGet = itemRepository.findById(itemId);

        if (itemGet.isEmpty()) {
            throw new NotFoundException("У пользователя с id = " + userId + " не " +
                    "существует вещи с id = " + itemId);
        }

        Item item = itemGet.get();
        ItemDtoOut itemDtoOut = ItemMapper.toItemDtoOut(itemGet.get());
        itemDtoOut.setComments(getAllItemComments(itemId));

        if (!item.getOwner().getId().equals(userId)) {
            return itemDtoOut;
        }

        List<Booking> bookings = bookingRepository.findAllByItemAndStatusOrderByStartAsc(item, BookingStatus.APPROVED);
        List<BookingDtoOut> bookingDTOList = bookings.stream()
                .map(BookingMapper::toBookingOut)
                .collect(toList());

        itemDtoOut.setLastBooking(getLastBooking(bookingDTOList, LocalDateTime.now()));
        itemDtoOut.setNextBooking(getNextBooking(bookingDTOList, LocalDateTime.now()));
        return itemDtoOut;
    }

    /**
     * Находит все вещи пользователя по его ID и возвращает список объектов ItemDtoOut с информацией о них.
     *
     * @param userId - ID пользователя
     * @param from - начальный индекс для пагинации
     * @param size - количество записей на страницу для пагинации
     * @return список объектов ItemDtoOut с информацией о вещах пользователя
     */
    @Override
    @Transactional
    public List<ItemDtoOut> findAll(Long userId, Integer from, Integer size) {
        UserDto owner = userService.findById(userId);
        Pageable pageable = PageRequest.of(from / size, size);
        List<Item> itemList = itemRepository.findAllByOwnerId(userId, pageable);
        itemList.sort((o1, o2) -> Math.toIntExact(o1.getId() - o2.getId()));
        List<Long> idList = itemList.stream()
                .map(Item::getId)
                .collect(Collectors.toList());
        Map<Long, List<CommentDtoOut>> comments = commentRepository.findAllByItemIdIn(idList).stream()
                .map(CommentMapper::toCommentDtoOut)
                .collect(groupingBy(CommentDtoOut::getItemId, toList()));

        Map<Long, List<BookingDtoOut>> bookings = bookingRepository.findAllByItemInAndStatusOrderByStartAsc(itemList,
                        BookingStatus.APPROVED).stream()
                .map(BookingMapper::toBookingOut)
                .collect(groupingBy(BookingDtoOut::getItemId, toList()));

        return itemList
                .stream()
                .map(item -> ItemMapper.toItemDtoOut(item, getLastBooking(bookings.get(item.getId()),
                                LocalDateTime.now()), comments.get(item.getId()),
                        getNextBooking(bookings.get(item.getId()), LocalDateTime.now())))
                .collect(toList());
    }

    /**
     * Выполняет поиск вещей по тексту и возвращает список объектов ItemDtoOut с информацией о найденных вещах.
     *
     * @param userId - ID пользователя
     * @param text - текст для поиска
     * @param from - начальный индекс для пагинации
     * @param size - количество записей на страницу для пагинации
     * @return список объектов ItemDtoOut с информацией о найденных вещах
     */
    @Override
    @Transactional
    public List<ItemDtoOut> search(Long userId, String text, Integer from, Integer size) {
        userService.findById(userId);
        Pageable pageable = PageRequest.of(from / size, size);

        if (text.isBlank()) {
            return Collections.emptyList();
        }

        List<Item> itemList = itemRepository.search(text, pageable);
        return itemList.stream()
                .map(ItemMapper::toItemDtoOut)
                .collect(toList());
    }

    /**
     * Создает комментарий к вещи на основе данных из объекта CommentDto.
     *
     * @param userId - ID пользователя
     * @param commentDto - объект данных комментария
     * @param itemId - ID вещи
     * @return объект CommentDtoOut созданного комментария
     * @throws NotFoundException - если вещь или пользователь не найдены
     * @throws ValidationException - если у пользователя нет бронирований для данной вещи
     */
    @Override
    @Transactional
    public CommentDtoOut createComment(Long userId, CommentDto commentDto, Long itemId) {
        User user = UserMapper.toUser(userService.findById(userId));
        Optional<Item> itemById = itemRepository.findById(itemId);

        if (itemById.isEmpty()) {
            throw new NotFoundException("У пользователя с id = " + userId + " не " +
                    "существует вещи с id = " + itemId);
        }

        Item item = itemById.get();

        List<Booking> userBookings = bookingRepository.findAllByUserBookings(userId, itemId, LocalDateTime.now());

        if (userBookings.isEmpty()) {
            throw new ValidationException("У пользователя с id   " + userId + " должно быть хотя бы одно бронирование предмета с id " + itemId);
        }

        return CommentMapper.toCommentDtoOut(commentRepository.save(CommentMapper.toComment(commentDto, item, user)));
    }

    /**
     * Возвращает список комментариев для вещи по ее ID.
     *
     * @param itemId - ID вещи
     * @return список объектов CommentDtoOut с информацией о комментариях
     */
    public List<CommentDtoOut> getAllItemComments(Long itemId) {
        List<Comment> comments = commentRepository.findAllByItemId(itemId);

        return comments.stream()
                .map(CommentMapper::toCommentDtoOut)
                .collect(toList());
    }

    /**
     * Возвращает последнее бронирование из списка, которое имеет дату начала не позже, чем заданное время.
     * @param bookings - список объектов BookingDtoOut
     * @param time - заданное время
     * @return объект BookingDtoOut с информацией о последнем бронировании или null, если список пустой или все бронирования имеют дату начала после заданного времени
     */
    private BookingDtoOut getLastBooking(List<BookingDtoOut> bookings, LocalDateTime time) {
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }
        return bookings.stream()
                .filter(bookingDTO -> !bookingDTO.getStart().isAfter(time))
                .reduce((booking1, booking2) -> booking1.getStart().isAfter(booking2.getStart()) ? booking1 : booking2)
                .orElse(null);
    }

    /**
     * Возвращает следующее бронирование из списка, которое имеет дату начала после заданного времени.
     *
     * @param bookings - список объектов BookingDtoOut
     * @param time - заданное время
     * @return объект BookingDtoOut с информацией о следующем бронировании или null, если список пустой или нет бронирований с датой начала после заданного времени
     */
    private BookingDtoOut getNextBooking(List<BookingDtoOut> bookings, LocalDateTime time) {
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }
        return bookings.stream()
                .filter(bookingDTO -> bookingDTO.getStart().isAfter(time))
                .findFirst()
                .orElse(null);
    }
}
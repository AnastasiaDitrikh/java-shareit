package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.service.UserServiceDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceDtoImpl implements ItemServiceDto {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final UserServiceDto userService;

    @Override
    public ItemDto add(Long userId, ItemDto itemDto) {
        UserDto user = userService.findById(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner((UserMapper.toUser(user)));
        return ItemMapper.toItemDto(itemRepository.save(item));
    }


    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        UserDto user = userService.findById(userId);
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isPresent()) {
            if (!UserMapper.toUser(user).equals(itemOptional.get().getOwner())) {
                throw new NotFoundException("Пользователь с id = " + userId +
                        " не является собственником вещи id = " + itemId);
            }
            Item itemFromStorage = itemOptional.get();
            Item item = ItemMapper.toItem(itemDto);
            if (item.getAvailable() == null) {
                item.setAvailable(itemFromStorage.getAvailable());
            }
            if (item.getDescription() == null) {
                item.setDescription(itemFromStorage.getDescription());
            }
            if (item.getName() == null) {
                item.setName(itemFromStorage.getName());
            }
            item.setId(itemFromStorage.getId());
            item.setRequest(itemFromStorage.getRequest());
            item.setOwner(itemFromStorage.getOwner());

            return ItemMapper.toItemDto(itemRepository.save(item));
        }
        return itemDto;
    }


    @Override
    public ItemDto findItemById(Long userId, Long itemId) {
        userService.findById(userId);
        Optional<Item> itemGet = itemRepository.findById(itemId);
        if (itemGet.isEmpty()) {
            throw new NotFoundException("У пользователя с id = " + userId + " не " +
                    "существует вещи с id = " + itemId);
        }
        Item item = itemGet.get();
        ItemDto itemDto = ItemMapper.toItemDto(itemGet.get());
        itemDto.setComments(getAllItemComments(itemId));
        if (!item.getOwner().getId().equals(userId)) {
            return itemDto;
        }
        getLastNextBooking(itemDto);
        return itemDto;
    }

    @Override
    public List<ItemDto> findAll(Long userId) {
        userService.findById(userId);
        List<Item> itemList = itemRepository.findAllByOwnerId(userId);
        List<ItemDto> itemDtoList = itemList.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        itemDtoList.forEach(this::getLastNextBooking);
        return itemDtoList;
    }

    @Override
    public List<ItemDto> search(Long userId, String text) {
        userService.findById(userId);
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        List<Item> itemList = itemRepository.search(text);
        return itemList.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(Long userId, CommentDto commentDto, Long itemId) {
        User user = UserMapper.toUser(userService.findById(userId));
        Optional<Item> itemById = itemRepository.findById(itemId);

        if (itemById.isEmpty()) {

            throw new NotFoundException("У пользователя с id = " + userId + " не " +
                    "существует вещи с id = " + itemId);
        }
        Item item = itemById.get();

        List<Booking> userBookings = bookingRepository.findAllByUserBookings(userId, itemId, LocalDateTime.now());

        if (userBookings.isEmpty()) {
            throw new ValidationException(String.format("Пользователь с id %s должно быть хотя бы одно бронирование " +
                    "предмета с id %s.", userId, itemId));
        }

        return CommentMapper.toCommentDto(commentRepository.save(CommentMapper.toComment(commentDto, item, user)));
    }

    public List<CommentDto> getAllItemComments(Long itemId) {
        List<Comment> comments = commentRepository.findAllByItemId(itemId);

        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    private void getLastNextBooking(ItemDto itemDto) {
        Optional<Booking> lastBooking = bookingRepository.getLastBooking(itemDto.getId(), LocalDateTime.now());
        itemDto.setLastBooking(
                lastBooking.map(BookingMapper::toBookingItemDto).orElse(null)
        );
        Optional<Booking> nextBooking = bookingRepository.getNextBooking(itemDto.getId(), LocalDateTime.now());
        itemDto.setNextBooking(
                nextBooking.map(BookingMapper::toBookingItemDto).orElse(null)
        );
    }
}
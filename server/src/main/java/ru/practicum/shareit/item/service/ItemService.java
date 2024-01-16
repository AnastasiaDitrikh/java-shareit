package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;

import java.util.List;

/**
 * Интерфейс ItemService, определяющий методы для работы с вещами
 */
public interface ItemService {

    ItemDtoOut add(Long userId, ItemDto itemDto);

    ItemDtoOut update(Long userId, Long itemId, ItemDto itemDto);

    ItemDtoOut findItemById(Long userId, Long itemId);

    List<ItemDtoOut> findAll(Long userId, Integer from, Integer size);

    List<ItemDtoOut> search(Long userId, String text, Integer from, Integer size);

    CommentDtoOut createComment(Long userId, CommentDto commentDto, Long itemId);
}
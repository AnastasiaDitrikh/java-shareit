package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;

import java.util.List;

public interface ItemService {

    ItemDtoOut add(Long userId, ItemDto itemDto);

    ItemDtoOut update(Long userId, Long itemId, ItemDto itemDto);

    ItemDtoOut findItemById(Long userId, Long itemId);

    List<ItemDtoOut> findAll(Long userId);

    List<ItemDtoOut> search(Long userId, String text);

    CommentDtoOut createComment(Long userId, CommentDto commentDto, Long itemId);
}
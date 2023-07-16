package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemServiceDto {

    ItemDto add(Long userId, ItemDto itemDto);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    ItemDto findItemById(Long userId, Long itemId);

    List<ItemDto> findAll(Long userId);

    List<ItemDto> search(Long userId, String text);

    CommentDto createComment(Long userId, CommentDto commentDto, Long itemId);
}
package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import java.util.List;

/**
 * Интерфейс ItemRequestService, определяющий методы для работы с запросами на вещи
 */
public interface ItemRequestService {
    ItemRequestDtoOut add(Long userId, ItemRequestDto requestDto);

    List<ItemRequestDtoOut> getUserRequests(Long userId);

    List<ItemRequestDtoOut> getAllRequests(Long userId, Integer from, Integer size);

    ItemRequestDtoOut getRequestById(Long userId, Long requestId);
}
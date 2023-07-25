package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDtoOut add(Long userId, ItemRequestDto requestDto);

    List<ItemRequestDtoOut> getUserRequests(Long userId);

    List<ItemRequestDtoOut> getAllRequests(Long userId, Integer from, Integer size);

    ItemRequestDtoOut getRequestById(Long userId, Long requestId);

}

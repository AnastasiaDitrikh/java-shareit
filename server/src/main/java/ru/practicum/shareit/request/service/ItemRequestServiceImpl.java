package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserService userService;
    private final ItemRequestRepository requestRepository;

    /**
     * Добавляет новый запрос на предмет от пользователя и возвращает информацию о добавленном запросе.
     *
     * @param userId         - ID пользователя
     * @param itemRequestDto - объект данных запроса на предмет
     * @return объект ItemRequestDtoOut с информацией о добавленном запросе
     */
    @Override
    @Transactional
    public ItemRequestDtoOut add(Long userId, ItemRequestDto itemRequestDto) {
        User user = UserMapper.toUser(userService.findById(userId));
        ItemRequest request = ItemRequestMapper.toRequest(user, itemRequestDto);
        request.setRequester(user);
        return ItemRequestMapper.toRequestDtoOut(requestRepository.save(request));
    }

    /**
     * Возвращает список всех запросов на предмет пользователя.
     *
     * @param userId - ID пользователя
     * @return список объектов ItemRequestDtoOut с информацией о запросах на предмет пользователя
     */
    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDtoOut> getUserRequests(Long userId) {
        UserMapper.toUser(userService.findById(userId));
        List<ItemRequest> itemRequestList = requestRepository.findAllByRequesterId(userId);
        return itemRequestList.stream()
                .map(ItemRequestMapper::toRequestDtoOut)
                .collect(Collectors.toList());
    }

    /**
     * Возвращает список всех запросов на предмет, кроме запросов пользователя.
     *
     * @param userId - ID пользователя
     * @param from   - начальный индекс для пагинации
     * @param size   - количество записей на страницу для пагинации
     * @return список объектов ItemRequestDtoOut с информацией о запросах на предмет
     */
    @Override
    public List<ItemRequestDtoOut> getAllRequests(Long userId, Integer from, Integer size) {
        UserMapper.toUser(userService.findById(userId));
        List<ItemRequest> itemRequestList = requestRepository.findAllByRequester_IdNotOrderByCreatedDesc(userId, PageRequest.of(from / size, size));
        return itemRequestList.stream()
                .map(ItemRequestMapper::toRequestDtoOut)
                .collect(Collectors.toList());
    }

    /**
     * Возвращает информацию о запросе на предмет по его ID.
     *
     * @param userId    - ID пользователя
     * @param requestId - ID запроса на предмет
     * @return объект ItemRequestDtoOut с информацией о запросе на предмет
     */
    @Override
    public ItemRequestDtoOut getRequestById(Long userId, Long requestId) {
        userService.findById(userId);
        Optional<ItemRequest> requestById = requestRepository.findById(requestId);
        if (requestById.isEmpty()) {
            throw new NotFoundException(String.format("Запрос с id: %s " + "не был найден.", requestId));
        }
        return ItemRequestMapper.toRequestDtoOut(requestById.get());
    }
}
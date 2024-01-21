package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static ru.practicum.shareit.item.ItemController.USER_HEADER;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestService requestService;

    /**
     * Добавляет новый запрос на предмет от пользователя и возвращает информацию о добавленном запросе.
     *
     * @param userId - ID пользователя из заголовка запроса
     * @param requestDto - объект данных запроса на предмет
     * @return объект ItemRequestDtoOut с информацией о добавленном запросе
     */
    @PostMapping
    public ItemRequestDtoOut add(@RequestHeader(USER_HEADER) Long userId,
                                 @RequestBody ItemRequestDto requestDto) {
        return requestService.add(userId, requestDto);
    }

    /**
     * Возвращает список всех запросов на предмет пользователя.
     *
     * @param userId - ID пользователя из заголовка запроса
     * @return список объектов ItemRequestDtoOut с информацией о запросах на предмет пользователя
     */
    @GetMapping
    public List<ItemRequestDtoOut> getUserRequests(@RequestHeader(USER_HEADER) Long userId) {
        return requestService.getUserRequests(userId);
    }

    /**
     * Возвращает список всех запросов на предмет, включая запросы от других пользователей.
     *
     * @param userId - ID пользователя из заголовка запроса
     * @param from - начальный индекс для пагинации (по умолчанию 0)
     * @param size - количество записей на страницу для пагинации (по умолчанию 10)
     * @return список объектов ItemRequestDtoOut с информацией о запросах на предмет
     */
    @GetMapping("/all")
    public List<ItemRequestDtoOut> getAllRequests(@RequestHeader(USER_HEADER) Long userId,
                                                  @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                  @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return requestService.getAllRequests(userId, from, size);
    }

    /**
     * Возвращает информацию о запросе на предмет по его ID.
     *
     * @param userId - ID пользователя из заголовка запроса
     * @param requestId - ID запроса на предмет
     * @return объект ItemRequestDtoOut с информацией о запросе на предмет
     */
    @GetMapping("/{requestId}")
    public ItemRequestDtoOut get(@RequestHeader(USER_HEADER) Long userId,
                                 @PathVariable Long requestId) {
        return requestService.getRequestById(userId, requestId);
    }
}
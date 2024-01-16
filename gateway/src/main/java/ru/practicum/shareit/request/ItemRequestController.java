package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import static ru.practicum.shareit.Constants.USER_HEADER;

/**
 * Класс ItemRequestController, представляющий контроллер для обработки HTTP-запросов, связанных с запросами на вещи.
 */
@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final RequestClient requestClient;

    /**
     * Обработчик POST запроса на создание нового запроса на вещь.
     *
     * @param userId     Идентификатор пользователя.
     * @param requestDto Объект ItemRequestDto с информацией о запросе.
     * @return Объект ResponseEntity с ответом сервера.
     */
    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader(USER_HEADER) Long userId,
                                      @Valid @RequestBody ItemRequestDto requestDto) {
        return requestClient.addNewRequest(userId, requestDto);
    }

    /**
     * Обработчик GET запроса на получение списка запросов пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return Объект ResponseEntity с ответом сервера.
     */
    @GetMapping
    public ResponseEntity<Object> getUserRequests(@RequestHeader(USER_HEADER) Long userId) {
        return requestClient.getUserRequests(userId);
    }

    /**
     * Обработчик GET запроса на получение списка всех запросов на вещи.
     *
     * @param userId Идентификатор пользователя.
     * @param from   Параметр "from" для пагинации (начальный индекс).
     * @param size   Параметр "size" для пагинации (размер страницы).
     * @return Объект ResponseEntity с ответом сервера.
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader(USER_HEADER) Long userId,
                                                 @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                 @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        return requestClient.getAllRequests(userId, from, size);
    }

    /**
     * Обработчик GET запроса на получение информации о запросе на вещь.
     *
     * @param userId    Идентификатор пользователя.
     * @param requestId Идентификатор запроса.
     * @return Объект ResponseEntity с ответом сервера.
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> get(@RequestHeader(USER_HEADER) Long userId,
                                      @PathVariable Long requestId) {
        return requestClient.getRequestById(userId, requestId);
    }
}
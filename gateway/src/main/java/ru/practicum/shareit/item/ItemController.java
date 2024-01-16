package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import static ru.practicum.shareit.Constants.USER_HEADER;

/**
 * Класс ItemController, представляющий контроллер для обработки HTTP-запросов, связанных с вещами.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemClient itemClient;

    /**
     * Обработчик POST запроса на создание новой вещи.
     *
     * @param userId  Идентификатор пользователя.
     * @param itemDto Объект ItemDto с информацией о вещи.
     * @return Объект ResponseEntity с ответом сервера.
     */
    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(USER_HEADER) Long userId,
                                         @Valid @RequestBody ItemDto itemDto) {
        log.info("POST запрос на создание новой вещи: {} от пользователя c id: {}", itemDto, userId);
        return itemClient.create(userId, itemDto);
    }

    /**
     * Обработчик PATCH запроса на обновление информации о вещи.
     *
     * @param userId  Идентификатор пользователя.
     * @param itemDto Объект ItemDto с информацией о вещи.
     * @param itemId  Идентификатор вещи.
     * @return Объект ResponseEntity с ответом сервера.
     */
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(USER_HEADER) Long userId,
                                         @RequestBody ItemDto itemDto,
                                         @PathVariable("itemId") Long itemId) {
        log.info("PATCH запрос на обновление вещи id: {} пользователя c id: {}", itemId, userId);
        return itemClient.update(userId, itemId, itemDto);
    }

    /**
     * Обработчик GET запроса на получение информации о вещи.
     *
     * @param userId Идентификатор пользователя.
     * @param itemId Идентификатор вещи.
     * @return Объект ResponseEntity с ответом сервера.
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> get(@RequestHeader(USER_HEADER) Long userId,
                                      @PathVariable Long itemId) {
        log.info("GET запрос на получение вещи c id: {}", itemId);
        return itemClient.get(userId, itemId);
    }

    /**
     * Обработчик GET запроса на получение списка всех вещей пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @param from   Параметр "from" для пагинации (начальный индекс).
     * @param size   Параметр "size" для пагинации (размер страницы).
     * @return Объект ResponseEntity с ответом сервера.
     */
    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(USER_HEADER) Long userId,
                                         @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                         @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("GET запрос на получение всех вещей пользователя c id: {}", userId);
        return itemClient.getAll(userId, from, size);
    }

    /**
     * Обработчик GET запроса на поиск вещей по тексту.
     *
     * @param userId Идентификатор пользователя.
     * @param text   Текст для поиска вещей.
     * @param from   Параметр "from" для пагинации (начальный индекс).
     * @param size   Параметр "size" для пагинации (размер страницы).
     * @return Объект ResponseEntity с ответом сервера.
     */
    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader(USER_HEADER) Long userId,
                                              @RequestParam(name = "text") String text,
                                              @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                              @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("GET запрос на поиск всех вещей c текстом: {}", text);
        return itemClient.searchItems(userId, text, from, size);
    }

    /**
     * Обработчик POST запроса на создание комментария к вещи.
     *
     * @param userId     Идентификатор пользователя.
     * @param commentDto Объект CommentDto с информацией о комментарии.
     * @param itemId     Идентификатор вещи.
     * @return Объект ResponseEntity с ответом сервера.
     */
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(USER_HEADER) Long userId,
                                                @Validated @RequestBody CommentDto commentDto,
                                                @PathVariable Long itemId) {
        return itemClient.createComment(userId, commentDto, itemId);
    }
}
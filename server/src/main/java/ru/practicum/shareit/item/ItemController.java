package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@Validated
public class ItemController {
    public static final String USER_HEADER = "X-Sharer-User-Id";
    @Autowired
    private ItemService itemService;

    /**
     * Добавляет новый предмет, отправленный пользователем, и возвращает информацию о добавленном предмете.
     *
     * @param userId  - ID пользователя из заголовка запроса
     * @param itemDto - объект данных предмета
     * @return объект ItemDtoOut с информацией о добавленном предмете
     */
    @PostMapping
    public ItemDtoOut add(@RequestHeader(USER_HEADER) Long userId,
                          @RequestBody ItemDto itemDto) {
        log.info("POST Запрос на добавление пользователем с id = {} предмета {}", userId, itemDto.toString());
        return itemService.add(userId, itemDto);
    }

    /**
     * Обновляет информацию о предмете на основе данных, отправленных пользователем, и возвращает обновленную информацию о предмете.
     *
     * @param userId  - ID пользователя из заголовка запроса
     * @param itemDto - объект данных предмета
     * @param itemId  - ID предмета, который нужно обновить
     * @return объект ItemDtoOut с обновленной информацией о предмете
     */
    @PatchMapping("/{itemId}")
    public ItemDtoOut update(@RequestHeader(USER_HEADER) Long userId,
                             @RequestBody ItemDto itemDto,
                             @PathVariable Long itemId) {
        log.info("PATCH Запрос на обновление предмета с id = {} пользователем с id = {} ", itemId, userId);
        return itemService.update(userId, itemId, itemDto);
    }

    /**
     * Возвращает информацию о предмете с заданным ID.
     *
     * @param userId - ID пользователя из заголовка запроса
     * @param itemId - ID предмета, который нужно найти
     * @return объект ItemDtoOut с информацией о предмете
     */
    @GetMapping("/{itemId}")
    public ItemDtoOut findById(@RequestHeader(USER_HEADER) Long userId,
                               @PathVariable("itemId") Long itemId) {
        log.info("GET Запрос на получение предмета с id = {} пользователем с id = {} ", itemId, userId);
        return itemService.findItemById(userId, itemId);
    }

    /**
     * Возвращает список всех предметов пользователя.
     *
     * @param userId - ID пользователя из заголовка запроса
     * @param from   - начальный индекс для пагинации (по умолчанию 0)
     * @param size   - количество записей на страницу для пагинации (по умолчанию 10)
     * @return список объектов ItemDtoOut с информацией о предметах пользователя
     */
    @GetMapping
    public List<ItemDtoOut> findAll(@RequestHeader(USER_HEADER) Long userId,
                                    @RequestParam(value = "from", defaultValue = "0") Integer from,
                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("GET Запрос на получение предметов пользователя с id = {}", userId);
        return itemService.findAll(userId, from, size);
    }

    /**
     * Выполняет поиск предметов по тексту и возвращает список найденных предметов.
     *
     * @param userId - ID пользователя из заголовка запроса
     * @param text   - текст для поиска предметов
     * @param from   - начальный индекс для пагинации (по умолчанию 0)
     * @param size   - количество записей на страницу для пагинации (по умолчанию 10)
     * @return список объектов ItemDtoOut с информацией о найденных предметах
     */
    @GetMapping("/search")
    public List<ItemDtoOut> searchItems(@RequestHeader(USER_HEADER) Long userId,
                                        @RequestParam(name = "text") String text,
                                        @RequestParam(value = "from", defaultValue = "0") Integer from,
                                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("GET Запрос на поиск предметов c текстом = {}", text);
        return itemService.search(userId, text, from, size);
    }

    /**
     * Создает новый комментарий к предмету и возвращает информацию о созданном комментарии.
     *
     * @param userId     - ID пользователя из заголовка запроса
     * @param commentDto - объект данных комментария
     * @param itemId     - ID предмета, к которому добавляется комментарий
     * @return объект CommentDtoOut с информацией о созданном комментарии
     */
    @PostMapping("/{itemId}/comment")
    public CommentDtoOut createComment(@RequestHeader(USER_HEADER) Long userId,
                                       @Validated @RequestBody CommentDto commentDto,
                                       @PathVariable Long itemId) {
        log.info("POST Запрос на создание комментария id = {}", itemId);
        return itemService.createComment(userId, commentDto, itemId);
    }
}
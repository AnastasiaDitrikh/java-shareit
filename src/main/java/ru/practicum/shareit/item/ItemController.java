package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    public static final String USER_HEADER = "X-Sharer-User-Id";
    private final ItemServiceImpl itemService;

    @PostMapping
    public ItemDtoOut add(@RequestHeader(USER_HEADER) Long userId,
                          @Valid @RequestBody ItemDto itemDto) {
        log.info("POST Запрос на добавление пользователем с id = {} предмета {}", userId, itemDto.toString());
        return itemService.add(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoOut update(@RequestHeader(USER_HEADER) Long userId,
                             @RequestBody ItemDto itemDto,
                             @PathVariable Long itemId) {
        log.info("PATCH Запрос на обновление предмета с id = {} пользователем с id = {} ", itemId, userId);
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDtoOut findById(@RequestHeader(USER_HEADER) Long userId,
                               @PathVariable("itemId")
                               Long itemId) {
        log.info("GET Запрос на получение предмета с id = {} пользователем с id = {} ", itemId, userId);
        return itemService.findItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemDtoOut> findAll(@RequestHeader(USER_HEADER) Long userId) {
        log.info("GET Запрос на получение предметов пользователя с id = {}", userId);
        return itemService.findAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDtoOut> searchItems(@RequestHeader(USER_HEADER) Long userId,
                                        @RequestParam(name = "text") String text) {
        log.info("GET Запрос на поиск предметов c текстом = {}", text);
        return itemService.search(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOut createComment(@RequestHeader(USER_HEADER) Long userId,
                                       @Validated @RequestBody CommentDto commentDto,
                                       @PathVariable Long itemId) {
        log.info("POST Запрос на создание комментария id = {}", itemId);
        return itemService.createComment(userId, commentDto, itemId);
    }
}
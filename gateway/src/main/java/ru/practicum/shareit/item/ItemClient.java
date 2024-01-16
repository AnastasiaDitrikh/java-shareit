package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collections;
import java.util.Map;

/**
 * Класс ItemClient, представляющий клиент для взаимодействия с сервисом, отвечающим за вещи.
 */
@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    /**
     * Отправляет POST запрос на создание новой вещи.
     *
     * @param userId  Идентификатор пользователя.
     * @param itemDto Объект ItemDto с информацией о вещи.
     * @return Объект ResponseEntity с ответом сервера.
     */
    public ResponseEntity<Object> create(Long userId, ItemDto itemDto) {
        return post("", userId, itemDto);
    }

    /**
     * Отправляет PATCH запрос на обновление информации о вещи.
     *
     * @param userId  Идентификатор пользователя.
     * @param itemId  Идентификатор вещи.
     * @param itemDto Объект ItemDto с информацией о вещи.
     * @return Объект ResponseEntity с ответом сервера.
     */
    public ResponseEntity<Object> update(Long userId, Long itemId, ItemDto itemDto) {
        return patch("/" + itemId, userId, itemDto);
    }

    /**
     * Отправляет GET запрос на получение информации о вещи.
     *
     * @param userId Идентификатор пользователя.
     * @param itemId Идентификатор вещи.
     * @return Объект ResponseEntity с ответом сервера.
     */
    public ResponseEntity<Object> get(Long userId, Long itemId) {
        return get("/" + itemId, userId, null);
    }

    /**
     * Отправляет GET запрос на получение списка всех вещей пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @param from   Параметр "from" для пагинации (начальный индекс).
     * @param size   Параметр "size" для пагинации (размер страницы).
     * @return Объект ResponseEntity с ответом сервера.
     */
    public ResponseEntity<Object> getAll(Long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size);
        return get("?from={from}&size={size}", userId, parameters);
    }

    /**
     * Отправляет GET запрос на поиск вещей по тексту.
     *
     * @param userId Идентификатор пользователя.
     * @param text   Текст для поиска вещей.
     * @param from   Параметр "from" для пагинации (начальный индекс).
     * @param size   Параметр "size" для пагинации (размер страницы).
     * @return Объект ResponseEntity с ответом сервера.
     */
    public ResponseEntity<Object> searchItems(Long userId, String text, Integer from, Integer size) {
        if (text.isBlank()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size,
                "text", text);
        return get("/search?from={from}&size={size}&text={text}", userId, parameters);
    }

    /**
     * Отправляет POST запрос на создание комментария к вещи.
     *
     * @param userId     Идентификатор пользователя.
     * @param commentDto Объект CommentDto с информацией о комментарии.
     * @param itemId     Идентификатор вещи.
     * @return Объект ResponseEntity с ответом сервера.
     */
    public ResponseEntity<Object> createComment(Long userId, CommentDto commentDto, Long itemId) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }
}
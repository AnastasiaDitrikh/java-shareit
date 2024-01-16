package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Map;

/**
 * Класс RequestClient, представляющий клиент для взаимодействия с сервисом, отвечающим за запросы на вещи.
 */
@Service
public class RequestClient extends BaseClient {

    private static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    /**
     * Отправляет POST запрос на создание нового запроса на вещь.
     *
     * @param userId     Идентификатор пользователя.
     * @param requestDto Объект ItemRequestDto с информацией о запросе.
     * @return Объект ResponseEntity с ответом сервера.
     */
    public ResponseEntity<Object> addNewRequest(Long userId, ItemRequestDto requestDto) {
        return post("", userId, requestDto);
    }

    /**
     * Отправляет GET запрос на получение списка запросов пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return Объект ResponseEntity с ответом сервера.
     */
    public ResponseEntity<Object> getUserRequests(Long userId) {
        return get("", userId);
    }

    /**
     * Отправляет GET запрос на получение списка всех запросов на вещи.
     *
     * @param userId Идентификатор пользователя.
     * @param from   Параметр "from" для пагинации (начальный индекс).
     * @param size   Параметр "size" для пагинации (размер страницы).
     * @return Объект ResponseEntity с ответом сервера.
     */
    public ResponseEntity<Object> getAllRequests(Long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size);
        return get("/all?from={from}&size={size}", userId, parameters);
    }

    /**
     * Отправляет GET запрос на получение информации о запросе на вещь.
     *
     * @param userId    Идентификатор пользователя.
     * @param requestId Идентификатор запроса.
     * @return Объект ResponseEntity с ответом сервера.
     */
    public ResponseEntity<Object> getRequestById(Long userId, Long requestId) {
        return get("/" + requestId, userId);
    }
}
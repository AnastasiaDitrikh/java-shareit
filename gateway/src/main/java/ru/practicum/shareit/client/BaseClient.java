package ru.practicum.shareit.client;

import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static ru.practicum.shareit.Constants.USER_HEADER;

/**
 * Класс BaseClient, представляющий базовый клиент для отправки HTTP-запросов.
 */
public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    /**
     * Отправляет GET запрос по указанному пути с пустыми параметрами.
     *
     * @param path Путь запроса.
     * @return Объект ResponseEntity с ответом сервера.
     */
    protected ResponseEntity<Object> get(String path) {
        return get(path, null, null);
    }

    /**
     * Отправляет GET запрос по указанному пути с указанным идентификатором пользователя.
     *
     * @param path   Путь запроса.
     * @param userId Идентификатор пользователя.
     * @return Объект ResponseEntity с ответом сервера.
     */
    protected ResponseEntity<Object> get(String path, long userId) {
        return get(path, userId, null);
    }

    /**
     * Отправляет GET запрос по указанному пути с указанным идентификатором пользователя и параметрами.
     *
     * @param path       Путь запроса.
     * @param userId     Идентификатор пользователя.
     * @param parameters Параметры запроса.
     * @return Объект ResponseEntity с ответом сервера.
     */
    protected ResponseEntity<Object> get(String path, Long userId, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, userId, parameters, null);
    }

    /**
     * Отправляет POST запрос по указанному пути с указанным телом запроса.
     *
     * @param path Путь запроса.
     * @param body Тело запроса.
     * @param <T>  Тип тела запроса.
     * @return Объект ResponseEntity с ответом сервера.
     */
    protected <T> ResponseEntity<Object> post(String path, T body) {
        return post(path, null, null, body);
    }

    /**
     * Отправляет POST запрос по указанному пути с указанным идентификатором пользователя и телом запроса.
     *
     * @param path   Путь запроса.
     * @param userId Идентификатор пользователя.
     * @param body   Тело запроса.
     * @param <T>    Тип тела запроса.
     * @return Объект ResponseEntity с ответом сервера.
     */
    protected <T> ResponseEntity<Object> post(String path, long userId, T body) {
        return post(path, userId, null, body);
    }

    /**
     * Отправляет POST запрос по указанному пути с указанным идентификатором пользователя, параметрами и телом запроса.
     *
     * @param path       Путь запроса.
     * @param userId     Идентификатор пользователя.
     * @param parameters Параметры запроса.
     * @param body       Тело запроса.
     * @param <T>        Тип тела запроса.
     * @return Объект ResponseEntity с ответом сервера.
     */
    protected <T> ResponseEntity<Object> post(String path, Long userId, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, userId, parameters, body);
    }

    /**
     * Отправляет PUT запрос по указанному пути с указанным идентификатором пользователя и телом запроса.
     *
     * @param path   Путь запроса.
     * @param userId Идентификатор пользователя.
     * @param body   Тело запроса.
     * @param <T>    Тип тела запроса.
     * @return Объект ResponseEntity с ответом сервера.
     */
    protected <T> ResponseEntity<Object> put(String path, long userId, T body) {
        return put(path, userId, null, body);
    }

    /**
     * Отправляет PUT запрос по указанному пути с указанным идентификатором пользователя, параметрами и телом запроса.
     *
     * @param path       Путь запроса.
     * @param userId     Идентификатор пользователя.
     * @param parameters Параметры запроса.
     * @param body       Тело запроса.
     * @param <T>        Тип тела запроса.
     * @return Объект ResponseEntity с ответом сервера.
     */
    protected <T> ResponseEntity<Object> put(String path, long userId, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.PUT, path, userId, parameters, body);
    }

    /**
     * Отправляет PATCH запрос по указанному пути с указанным телом запроса.
     *
     * @param path Путь запроса.
     * @param body Тело запроса.
     * @param <T>  Тип тела запроса.
     * @return Объект ResponseEntity с ответом сервера.
     */
    protected <T> ResponseEntity<Object> patch(String path, T body) {
        return patch(path, null, null, body);
    }

    /**
     * Отправляет PATCH запрос по указанному пути с указанным идентификатором пользователя.
     *
     * @param path   Путь запроса.
     * @param userId Идентификатор пользователя.
     * @return Объект ResponseEntity с ответом сервера.
     */
    protected <T> ResponseEntity<Object> patch(String path, long userId) {
        return patch(path, userId, null, null);
    }

    /**
     * Отправляет PATCH запрос по указанному пути с указанным идентификатором пользователя и телом запроса.
     *
     * @param path   Путь запроса.
     * @param userId Идентификатор пользователя.
     * @param body   Тело запроса.
     * @param <T>    Тип тела запроса.
     * @return Объект ResponseEntity с ответом сервера.
     */
    protected <T> ResponseEntity<Object> patch(String path, long userId, T body) {
        return patch(path, userId, null, body);
    }

    /**
     * Отправляет PATCH запрос по указанному пути с указанным идентификатором пользователя, параметрами и телом запроса.
     *
     * @param path       Путь запроса.
     * @param userId     Идентификатор пользователя.
     * @param parameters Параметры запроса.
     * @param body       Тело запроса.
     * @param <T>        Тип тела запроса.
     * @return Объект ResponseEntity с ответом сервера.
     */
    protected <T> ResponseEntity<Object> patch(String path, Long userId, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.PATCH, path, userId, parameters, body);
    }

    /**
     * Отправляет DELETE запрос по указанному пути с пустыми параметрами.
     *
     * @param path Путь запроса.
     * @return Объект ResponseEntity с ответом сервера.
     */
    protected ResponseEntity<Object> delete(String path) {
        return delete(path, null, null);
    }

    /**
     * Отправляет DELETE запрос по указанному пути с указанным идентификатором пользователя.
     *
     * @param path   Путь запроса.
     * @param userId Идентификатор пользователя.
     * @return Объект ResponseEntity с ответом сервера.
     */
    protected ResponseEntity<Object> delete(String path, long userId) {
        return delete(path, userId, null);
    }

    /**
     * Отправляет DELETE запрос по указанному пути с указанным идентификатором пользователя и параметрами.
     *
     * @param path       Путь запроса.
     * @param userId     Идентификатор пользователя.
     * @param parameters Параметры запроса.
     * @return Объект ResponseEntity с ответом сервера.
     */
    protected ResponseEntity<Object> delete(String path, Long userId, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.DELETE, path, userId, parameters, null);
    }

    /**
     * Создает и отправляет HTTP-запрос с указанными параметрами.
     *
     * @param method     HTTP-метод запроса.
     * @param path       Путь запроса.
     * @param userId     Идентификатор пользователя.
     * @param parameters Параметры запроса.
     * @param body       Тело запроса.
     * @param <T>        Тип тела запроса.
     * @return Объект ResponseEntity с ответом сервера.
     */
    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, Long userId, @Nullable Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders(userId));

        ResponseEntity<Object> shareitServerResponse;
        try {
            if (parameters != null) {
                shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(shareitServerResponse);
    }

    /**
     * Возвращает заголовки по умолчанию.
     *
     * @param userId Идентификатор пользователя.
     * @return Объект HttpHeaders с заголовками по умолчанию.
     */
    private HttpHeaders defaultHeaders(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (userId != null) {
            headers.set(USER_HEADER, String.valueOf(userId));
        }
        return headers;
    }

    /**
     * Подготавливает ответ шлюза на запрос.
     *
     * @param response Ответ сервера.
     * @return Объект ResponseEntity с ответом шлюза.
     */
    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}
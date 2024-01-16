package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

/**
 * Класс UserClient, представляющий клиент для взаимодействия с сервисом, отвечающим за пользователей.
 */
@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    /**
     * Отправляет POST запрос на добавление нового пользователя.
     *
     * @param userDto Объект UserDto с информацией о пользователе.
     * @return Объект ResponseEntity с ответом сервера.
     */
    public ResponseEntity<Object> add(UserDto userDto) {
        return post("", userDto);
    }

    /**
     * Отправляет GET запрос на получение информации о пользователе по его идентификатору.
     *
     * @param userId Идентификатор пользователя.
     * @return Объект ResponseEntity с ответом сервера.
     */
    public ResponseEntity<Object> getById(long userId) {
        return get("/" + userId);
    }

    /**
     * Отправляет GET запрос на получение списка всех пользователей.
     *
     * @return Объект ResponseEntity с ответом сервера.
     */
    public ResponseEntity<Object> getAll() {
        return get("/");
    }

    /**
     * Отправляет PATCH запрос на обновление информации о пользователе.
     *
     * @param userId  Идентификатор пользователя.
     * @param userDto Объект UserDto с информацией о пользователе.
     * @return Объект ResponseEntity с ответом сервера.
     */
    public ResponseEntity<Object> update(long userId, UserDto userDto) {
        return patch("/" + userId, userDto);
    }

    /**
     * Отправляет DELETE запрос на удаление пользователя по его идентификатору.
     *
     * @param userId Идентификатор пользователя.
     * @return Объект ResponseEntity с ответом сервера.
     */
    public ResponseEntity<Object> deleteById(Long userId) {
        return delete("/" + userId);
    }
}
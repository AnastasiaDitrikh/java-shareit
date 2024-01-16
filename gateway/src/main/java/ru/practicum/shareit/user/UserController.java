package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.markers.Create;

/**
 * Класс UserController, представляющий контроллер для обработки HTTP-запросов, связанных с пользователями.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Slf4j
public class UserController {

    private final UserClient userClient;

    /**
     * Обработчик POST запроса на создание нового пользователя.
     *
     * @param user Объект UserDto с информацией о пользователе.
     * @return Объект ResponseEntity с ответом сервера.
     */
    @PostMapping
    public ResponseEntity<Object> add(@Validated({Create.class}) @RequestBody UserDto user) {
        log.info("POST запрос на создание пользователя: {}", user);
        return userClient.add(user);
    }

    /**
     * Обработчик PATCH запроса на обновление информации о пользователе.
     *
     * @param userDto Объект UserDto с информацией о пользователе.
     * @param userId  Идентификатор пользователя.
     * @return Объект ResponseEntity с ответом сервера.
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@RequestBody UserDto userDto, @PathVariable Long userId) {
        log.info("PATCH запрос на обновление пользователя c id: {}", userId);
        return userClient.update(userId, userDto);
    }

    /**
     * Обработчик GET запроса на получение списка всех пользователей.
     *
     * @return Объект ResponseEntity с ответом сервера.
     */
    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("GET запрос на получение списка всех пользователей.");
        return userClient.getAll();
    }

    /**
     * Обработчик GET запроса на получение информации о пользователе по его идентификатору.
     *
     * @param userId Идентификатор пользователя.
     * @return Объект ResponseEntity с ответом сервера.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Object> get(@PathVariable Long userId) {
        log.info("GET запрос на получение пользователя c id: {}", userId);
        return userClient.getById(userId);
    }

    /**
     * Обработчик DELETE запроса на удаление пользователя по его идентификатору.
     *
     * @param userId Идентификатор пользователя.
     * @return Объект ResponseEntity с ответом сервера.
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable long userId) {
        log.info("DELETE запрос на удаление пользователя с id: {}", userId);
        return userClient.deleteById(userId);
    }
}
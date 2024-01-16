package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.markers.Create;
import ru.practicum.shareit.user.markers.Update;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * Класс UserController, представляющий контроллер для обработки HTTP-запросов, связанных с пользователями.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    /**
     * Обработчик POST запроса на добавление нового пользователя.
     *
     * @param userDto Объект UserDto с информацией о пользователе.
     * @return Объект UserDto с информацией о пользователе.
     */
    @PostMapping
    public UserDto add(@Validated({Create.class}) @RequestBody UserDto userDto) {
        log.info("Запрос на добавление пользователя {}", userDto);
        return userService.add(userDto);
    }

    /**
     * Обработчик GET запроса на получение информации о пользователе по его идентификатору.
     *
     * @param userId Идентификатор пользователя.
     * @return Объект UserDto с информацией о пользователе.
     */
    @GetMapping("/{userId}")
    public UserDto findById(@PathVariable Long userId) {
        log.info("Запрос на получение пользователя id = {}", userId);
        return userService.findById(userId);
    }

    /**
     * Обработчик GET запроса на получение списка всех пользователей.
     *
     * @return Список объектов UserDto с информацией о пользователях.
     */
    @GetMapping
    public List<UserDto> findAll() {
        log.info("Запрос на получение списка всех пользователей");
        return userService.findAll();
    }

    /**
     * Обработчик PATCH запроса на обновление информации о пользователе.
     *
     * @param userId  Идентификатор пользователя.
     * @param userDto Объект UserDto с обновленной информацией о пользователе.
     * @return Объект UserDto с информацией об обновленном пользователе.
     */
    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Long userId, @Validated({Update.class}) @RequestBody UserDto userDto) {
        log.info("Запрос на обновление пользователя id = {}", userId);
        return userService.update(userId, userDto);
    }

    /**
     * Обработчик DELETE запроса на удаление пользователя по его идентификатору.
     *
     * @param userId Идентификатор пользователя.
     */
    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("Delete - запрос на удаление пользователя id = {}", userId);
        userService.delete(userId);
    }
}
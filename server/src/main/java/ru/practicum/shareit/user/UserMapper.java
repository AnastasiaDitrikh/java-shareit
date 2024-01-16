package ru.practicum.shareit.user;

import lombok.experimental.UtilityClass;

/**
 * Класс UserMapper, представляющий маппер для преобразования объектов User и UserDto.
 */
@UtilityClass
public class UserMapper {

    /**
     * Преобразует объект User в объект UserDto.
     *
     * @param user Объект User.
     * @return Объект UserDto.
     */
    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    /**
     * Преобразует объект UserDto в объект User.
     *
     * @param userDto Объект UserDto.
     * @return Объект User.
     */
    public User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }
}
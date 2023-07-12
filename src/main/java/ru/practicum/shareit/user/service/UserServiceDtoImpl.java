package ru.practicum.shareit.user.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceDtoImpl {

    private final UserServiceDao userServiceDao;

    public UserDto add(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        checkEmail(user);
        return UserMapper.toUserDto(userServiceDao.add(user));
    }


    public UserDto update(Long id, UserDto userDto) throws NotFoundException {
        if (!isUserInMemory(id)) {
            throw new NotFoundException("Пользователя с " + id + " не существует");
        }
        User user = new User();
        UserDto userFromMemory = findById(id);

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        } else {
            user.setName(userFromMemory.getName());
        }
        if (userDto.getEmail() != null) {
            checkEmail(user);
            user.setEmail(userDto.getEmail());
        } else {
            user.setEmail(userFromMemory.getEmail());
        }
        user.setId(id);
        return UserMapper.toUserDto(userServiceDao.update(id, user));
    }


    public UserDto findById(Long id) throws NotFoundException {
        if (!isUserInMemory(id)) {
            throw new NotFoundException("Пользователя с " + id + " не существует");
        }
        User user = userServiceDao.findById(id);
        return UserMapper.toUserDto(user);
    }


    public void delete(Long id) throws NotFoundException {
        if (isUserInMemory(id)) {
            userServiceDao.delete(id);
        } else {
            throw new NotFoundException("Пользователя с " + id + " не существует");
        }
    }

    public List<UserDto> findAll() {
        return userServiceDao.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }


    private void checkEmail(User user) throws ValidationException {
        for (User thisUser : userServiceDao.findAll()) {
            if (thisUser.getEmail().equals(user.getEmail()) && !thisUser.getId().equals(user.getId())) {
                throw new ValidationException("Пользователь с такой электронной почтой уже существует");
            }
        }
    }


    private boolean isUserInMemory(Long userId) {
        for (User user : userServiceDao.findAll()) {
            if (user.getId().equals(userId)) {
                return true;
            }
        }
        return false;
    }
}








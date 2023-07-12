package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor


public class UserServiceDaoImpl implements UserServiceDao {
    private final Map<Long, User> users = new HashMap<>();
    private Long generatorId = 1L;

    @Override
    public User add(User user) {
        checkEmail(user);
        user.setId(generatorId);
        users.put(generatorId, user);
        generatorId++;
        return user;
    }

    @Override
    public User update(Long id, User user) throws NotFoundException {
        if (users.containsKey(id)) {
            checkEmail(user);
            users.put(id, user);
        } else {
            throw new NotFoundException("Пользователя с " + id + " не существует");
        }
        return users.get(id);
    }


    @Override
    public User findById(Long id) throws NotFoundException {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException("Пользователя с " + id + " не существует");
        }
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        if (users.containsKey(id)) {
            users.remove(id);
        } else {
            throw new NotFoundException("Пользователя с " + id + " не существует");
        }
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }


    private void checkEmail(User user) throws ValidationException {
        for (User thisUser : users.values()) {
            if (thisUser.getEmail().equals(user.getEmail()) && !thisUser.getId().equals(user.getId())) {
                throw new ValidationException("Пользователь с такой электронной почтой уже существует");
            }
        }
    }
}

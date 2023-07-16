package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotUniqueEmailException;
import ru.practicum.shareit.user.User;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class UserServiceDaoImpl implements UserServiceDao {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private Long generatorId = 1L;

    @Override
    public User add(User user) {
        checkEmail(user);
        user.setId(generatorId);
        users.put(generatorId, user);
        emails.add(user.getEmail());
        generatorId++;
        return user;
    }

    @Override
    public User update(Long id, User user) {
        checkUserInMemory(id);
        updateEmail(findById(id).getEmail(), user.getEmail());
        users.put(id, user);
        return users.get(id);
    }


    @Override
    public User findById(Long id) {
        checkUserInMemory(id);
        return users.get(id);
    }


    @Override
    public void delete(Long id) {
        checkUserInMemory(id);
        emails.remove(findById(id).getEmail());
        users.remove(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    private void checkEmail(User user) {
        if (emails.contains(user.getEmail())) {
            throw new NotUniqueEmailException("Пользователь с такой электронной почтой уже существует");
        }
    }

    private void updateEmail(String oldEmail, String newEmail) {
        emails.remove(oldEmail);
        if (emails.contains(newEmail)) {
            emails.add(oldEmail);
            throw new NotUniqueEmailException("Пользователь с такой электронной почтой уже существует");
        }
        emails.add(newEmail);
    }

    private void checkUserInMemory(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователя с " + id + " не существует");
        }
    }
}
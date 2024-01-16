package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс UserServiceImpl, реализующий интерфейс UserService для работы с пользователями.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Метод add(UserDto userDto) добавляет нового пользователя в базу данных.
     *
     * @param userDto Объект UserDto с информацией о пользователе.
     * @return Возвращает объект UserDto с информацией о созданном пользователе.
     */
    @Override
    @Transactional
    public UserDto add(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        userRepository.save(user);
        return UserMapper.toUserDto(user);
    }

    /**
     * Метод update(Long id, UserDto userDto) обновляет информацию о пользователе.
     *
     * @param id      Идентификатор пользователя.
     * @param userDto Объект UserDto с информацией о пользователе для обновления.
     * @return Возвращает объект UserDto с обновленной информацией о пользователе.
     */
    @Override
    @Transactional
    public UserDto update(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователя с " + id + " не существует"));
        String name = userDto.getName();
        if (name != null && !name.isBlank()) {
            user.setName(name);
        }
        String email = userDto.getEmail();
        if (email != null && !email.isBlank()) {
            user.setEmail(email);
        }
        return UserMapper.toUserDto(user);
    }

    /**
     * Метод findById(Long id) возвращает информацию о пользователе по его идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return Возвращает объект UserDto с информацией о пользователе.
     */
    @Override
    @Transactional
    public UserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователя с " + id + " не существует"));
        return UserMapper.toUserDto(user);
    }

    /**
     * Метод delete(Long id) удаляет пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Метод findAll() возвращает список всех пользователей.
     *
     * @return Возвращает список объектов UserDto с информацией о пользователях.
     */
    @Override
    @Transactional
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
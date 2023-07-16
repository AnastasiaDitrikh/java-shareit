package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceDtoImpl implements UserServiceDto {

    private final UserRepository userRepository;

    @Override
    public UserDto add(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        user = userRepository.save(user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        if (!isUserInDB(id)) {
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
            user.setEmail(userDto.getEmail());
        } else {
            user.setEmail(userFromMemory.getEmail());
        }
        user.setId(id);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                            return new NotFoundException("Пользователя с " + id + " не существует");
                        }
                );
        return UserMapper.toUserDto(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    private boolean isUserInDB(Long userId) {
        return userRepository.findAll().stream()
                .anyMatch(user -> user.getId().equals(userId));
    }
}
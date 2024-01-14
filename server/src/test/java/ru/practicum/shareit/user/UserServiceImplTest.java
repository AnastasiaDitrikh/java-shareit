package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private final UserDto userDto = UserDto.builder()
            .id(1L)
            .name("name")
            .email("my@email.com")
            .build();

    @Test
    void addNewUserReturnUserDto() {
        User userToSave = User.builder().id(1L).name("name").email("my@email.com").build();
        when(userRepository.save(userToSave)).thenReturn(userToSave);

        UserDto actualUserDto = userService.add(userDto);

        assertEquals(userDto, actualUserDto);
        verify(userRepository).save(userToSave);
    }

    @Test
    void updateUserTest() {
        UserDto user = userService.add(userDto);
        Long userId = user.getId();

        UserDto fieldsToUpdate = new UserDto();
        fieldsToUpdate.setEmail("updated@example.com");
        fieldsToUpdate.setName("Updated User");
        when(userRepository.findById(userId)).thenReturn(Optional.of(UserMapper.toUser(user)));
        UserDto updatedUserDto = userService.update(userId, fieldsToUpdate);
        assertNotNull(updatedUserDto);
        assertEquals("Updated User", updatedUserDto.getName());
        assertEquals("updated@example.com", updatedUserDto.getEmail());
    }


    @Test
    void findUserByIdWhenUserFound() {
        long userId = 1L;
        User expectedUser = User.builder().id(1L).name("name").email("my@email.com").build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
        UserDto expectedUserDto = UserMapper.toUserDto(expectedUser);

        UserDto actualUserDto = userService.findById(userId);

        assertEquals(expectedUserDto, actualUserDto);
    }

    @Test
    void findUserByIdWhenUserNotFound() {
        long userId = 0L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException userNotFoundException = assertThrows(NotFoundException.class,
                () -> userService.findById(userId));

        assertEquals(userNotFoundException.getMessage(), "Пользователя с " + userId + " не существует");
    }

    @Test
    void findAllUsersTest() {
        List<User> expectedUsers = List.of(new User());
        List<UserDto> expectedUserDto = expectedUsers.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());

        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<UserDto> actualUsersDto = userService.findAll();

        assertEquals(actualUsersDto.size(), 1);
        assertEquals(actualUsersDto, expectedUserDto);
    }

    @Test
    void deleteUser() {
        long userId = 0L;
        userService.delete(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }
}
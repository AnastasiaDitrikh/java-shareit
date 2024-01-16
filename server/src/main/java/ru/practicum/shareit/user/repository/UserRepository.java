package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.User;

/**
 * Интерфейс UserRepository, представляющий репозиторий для работы с сущностью User.
 * Расширяет JpaRepository для наследования базовых методов работы с базой данных.
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
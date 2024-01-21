package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

/**
 * Интерфейс ItemRequestRepository, представляющий репозиторий для работы с сущностью ItemRequest.
 * Расширяет JpaRepository для наследования базовых методов работы с базой данных.
 */
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequesterId(Long userId);

    List<ItemRequest> findAllByRequester_IdNotOrderByCreatedDesc(Long userId, Pageable pageable);
}
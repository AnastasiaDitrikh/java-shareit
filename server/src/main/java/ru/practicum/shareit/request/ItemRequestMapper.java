package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@UtilityClass
public class ItemRequestMapper {

    /**
     * Создает объект запроса на предмет на основе данных из объекта ItemRequestDto и пользователя.
     *
     * @param user           - объект пользователя
     * @param itemRequestDto - объект данных запроса на предмет
     * @return объект ItemRequest
     */
    public ItemRequest toRequest(User user, ItemRequestDto itemRequestDto) {
        return ItemRequest.builder()
                .description(itemRequestDto.getDescription())
                .build();
    }

    /**
     * Преобразует объект запроса на предмет в объект ItemRequestDto.
     *
     * @param request - объект запроса на предмет
     * @return объект ItemRequestDto
     */
    public ItemRequestDto toRequestDto(ItemRequest request) {
        return ItemRequestDto.builder()
                .description(request.getDescription())
                .build();
    }

    /**
     * Преобразует объект запроса на предмет в объект ItemRequestDtoOut.
     *
     * @param request - объект запроса на предмет
     * @return объект ItemRequestDtoOut
     */
    public ItemRequestDtoOut toRequestDtoOut(ItemRequest request) {
        List<ItemDtoOut> itemsDtoOut = new ArrayList<>();
        if (!Objects.isNull(request.getItems())) {
            itemsDtoOut = request.getItems().stream()
                    .map(ItemMapper::toItemDtoOut)
                    .collect(Collectors.toList());
        }
        return ItemRequestDtoOut.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .items(itemsDtoOut)
                .build();
    }
}
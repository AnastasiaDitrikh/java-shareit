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
    public ItemRequest toRequest(User user, ItemRequestDto itemRequestDto) {
        return ItemRequest.builder()
                .description(itemRequestDto.getDescription())
                .build();
    }

    public ItemRequestDto toRequestDto(ItemRequest request) {

        return ItemRequestDto.builder()
                .description(request.getDescription())
                .build();
    }

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


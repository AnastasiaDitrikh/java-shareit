package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@UtilityClass
public class ItemMapper {

    /**
     * Преобразует объект вещи в объект ItemDto.
     *
     * @param item - объект вещи
     * @return объект ItemDto
     */
    public ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto(
                item.getName(),
                item.getDescription(),
                item.getAvailable());

        if (item.getRequest() != null) {
            itemDto.setRequestId(item.getRequest().getId());
        }

        return itemDto;
    }

    /**
     * Преобразует объект вещи в объект ItemDtoOut.
     *
     * @param item - объект вещи
     * @return объект ItemDtoOut
     */
    public ItemDtoOut toItemDtoOut(Item item) {
        ItemDtoOut itemDtoOut = new ItemDtoOut(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable());
        if (item.getRequest() != null) {
            itemDtoOut.setRequestId(item.getRequest().getId());
        }
        return itemDtoOut;
    }

    /**
     * Преобразует объект вещи, последнее бронирование, список комментариев и следующее бронирование в объект ItemDtoOut.
     *
     * @param item        - объект вещи
     * @param lastBooking - последнее бронирование
     * @param comments    - список комментариев
     * @param nextBooking - следующее бронирование
     * @return объект ItemDtoOut
     */
    public ItemDtoOut toItemDtoOut(Item item, BookingDtoOut lastBooking, List<CommentDtoOut> comments, BookingDtoOut nextBooking) {
        return new ItemDtoOut(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking,
                comments,
                nextBooking);
    }

    /**
     * Создает объект вещи на основе данных из ItemDto.
     *
     * @param itemDto - объект данных вещи
     * @return объект Item
     */
    public Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable());
    }
}
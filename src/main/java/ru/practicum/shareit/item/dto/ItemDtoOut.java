package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.UserDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoOut {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private UserDto owner;
    private BookingDtoOut lastBooking;
    private BookingDtoOut nextBooking;
    private ItemRequest request;

    private List<CommentDtoOut> comments;

    public ItemDtoOut(Long id, String name, String description, Boolean available, UserDto owner, ItemRequest request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.request = request;
    }

    public ItemDtoOut(Long id, String name, String description, Boolean available, UserDto owner, ItemRequest request, BookingDtoOut lastBooking, BookingDtoOut nextBooking) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.request = request;
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
    }
}

package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDtoOut {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDtoOut lastBooking;
    private List<CommentDtoOut> comments;
    private BookingDtoOut nextBooking;
    private Long requestId;


    public ItemDtoOut(Long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }

    public ItemDtoOut(Long id, String name, String description, Boolean available, BookingDtoOut lastBooking, List<CommentDtoOut> comments, BookingDtoOut nextBooking) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.lastBooking = lastBooking;
        this.comments = comments;
        this.nextBooking = nextBooking;
    }
}

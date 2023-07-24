package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ItemServiceImpl itemService;

    private final User user = User.builder()
            .id(1L)
            .name("username")
            .email("email@email.com")
            .build();


    private final UserDto userDto = UserDto.builder()
            .id(1L)
            .name("username")
            .email("email@email.com")
            .build();

    private final Item item = Item.builder()
            .id(1L)
            .name("item name")
            .description("description")
            .available(true)
            .owner(user)
            .build();

    private final ItemDtoOut itemDto = ItemDtoOut.builder()
            .id(1L)
            .name("item name")
            .description("description")
            .available(true)
            .comments(Collections.emptyList())
            .build();

    private final Comment comment = Comment.builder()
            .id(1L)
            .text("comment")
            .created(LocalDateTime.now())
            .author(user)
            .item(item)
            .build();

    private final Booking booking = Booking.builder()
            .id(1L)
            .item(item)
            .booker(user)
            .status(BookingStatus.APPROVED)
            .start(LocalDateTime.now().minusDays(1L))
            .end(LocalDateTime.now().plusDays(1L))
            .build();


    @Test
    void getItemById() {
        when(userService.findById(user.getId())).thenReturn(userDto);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ItemDtoOut actualItemDto = itemService.findItemById(user.getId(), item.getId());

        assertEquals(itemDto, actualItemDto);
    }

    @Test
    void getAllComments() {
        List<CommentDtoOut> expectedCommentsDto = List.of(CommentMapper.toCommentDtoOut(comment));
        when(commentRepository.findAllByItemId(item.getId())).thenReturn(List.of(comment));

        List<CommentDtoOut> actualComments = itemService.getAllItemComments(item.getId());

        assertEquals(actualComments.size(), 1);
        assertEquals(actualComments, expectedCommentsDto);
    }


    @Test
    void createComment() {
        CommentDtoOut expectedCommentDto = CommentMapper.toCommentDtoOut(comment);
        when(userService.findById(user.getId())).thenReturn(userDto);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByUserBookings(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDtoOut actualCommentDto = itemService.createComment(user.getId(), CommentMapper.toCommentDto(comment), item.getId());

        assertEquals(expectedCommentDto, actualCommentDto);
    }

    @Test
    void createComment_whenItemIdIsNotValid_thenThrowObjectNotFoundException() {
        when(userService.findById(user.getId())).thenReturn(userDto);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());

        NotFoundException itemNotFoundException = assertThrows(NotFoundException.class,
                () -> itemService.createComment(user.getId(), CommentMapper.toCommentDto(comment), item.getId()));

        assertEquals(itemNotFoundException.getMessage(), "У пользователя с id = " + user.getId() + " не " +
                "существует вещи с id = " + item.getId());
    }

    @Test
    void createCommentWhenUserHaveNotAnyBookingsShouldThrowValidationException() {
        when(userService.findById(user.getId())).thenReturn(userDto);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByUserBookings(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        ValidationException userBookingsNotFoundException = assertThrows(ValidationException.class,
                () -> itemService.createComment(user.getId(), CommentMapper.toCommentDto(comment), item.getId()));

        assertEquals(userBookingsNotFoundException.getMessage(), "У пользователя с id   " + user.getId() + " должно быть хотя бы одно бронирование предмета с id " + item.getId());

    }
}
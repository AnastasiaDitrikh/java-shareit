package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@UtilityClass
public class CommentMapper {

    /**
     * Преобразует объект комментария в объект CommentDto.
     *
     * @param comment - объект комментария
     * @return объект CommentDto
     */
    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getText());
    }

    /**
     * Преобразует объект комментария в объект CommentDtoOut.
     *
     * @param comment - объект комментария
     * @return объект CommentDtoOut
     */
    public CommentDtoOut toCommentDtoOut(Comment comment) {
        return new CommentDtoOut(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated(),
                comment.getItem().getId());
    }

    /**
     * Создает объект комментария на основе данных из CommentDto, Item и User.
     *
     * @param commentDto - объект данных комментария
     * @param item - объект вещи
     * @param user - объект пользователя
     * @return объект Comment
     */
    public Comment toComment(CommentDto commentDto, Item item, User user) {
        return new Comment(
                commentDto.getText(),
                item,
                user);
    }
}
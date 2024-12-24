package ru.practicum.enw.model.mapper.custom;

import ru.practicum.enw.model.comment.CommentDto;
import ru.practicum.enw.model.entity.Comment;

public class CommentMapper {

    public static CommentDto fromEntityToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .author(UserMapper.fromEntityToShortDto(comment.getAuthor()))
                .eventId(comment.getEvent().getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }
}

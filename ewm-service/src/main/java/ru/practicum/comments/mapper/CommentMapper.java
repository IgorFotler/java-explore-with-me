package ru.practicum.comments.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.users.mapper.UserMapper;

@Component
public class CommentMapper {

    public static CommentDto convertToCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                EventMapper.convertToEventShortDto(comment.getEvent()),
                UserMapper.convertToUserDto(comment.getAuthor()),
                comment.getCreatedOn(),
                comment.getStatus()
        );
    }
}

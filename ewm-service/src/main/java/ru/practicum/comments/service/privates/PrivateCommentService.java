package ru.practicum.comments.service.privates;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;

public interface PrivateCommentService {

    CommentDto create(NewCommentDto newCommentDto, Long eventId, Long userId);

    CommentDto update(NewCommentDto newCommentDto, Long commentId, Long userId);

    void delete(Long commentId, Long userId);
}

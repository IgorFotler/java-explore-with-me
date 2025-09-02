package ru.practicum.comments.service.publics;

import ru.practicum.comments.dto.CommentDto;

import java.util.List;

public interface PublicCommentService {
    List<CommentDto> getAll(Long eventId, int from, int size);
}

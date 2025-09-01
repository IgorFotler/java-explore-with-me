package ru.practicum.comments.service.admin;

import ru.practicum.comments.dto.CommentDto;

public interface AdminCommentService {

    CommentDto moderateStatus(Long commentId, String status);
}

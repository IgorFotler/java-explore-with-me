package ru.practicum.comments.service.admin;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.model.CommentStatus;

public interface AdminCommentService {

    CommentDto moderateStatus(Long commentId, String status);
}

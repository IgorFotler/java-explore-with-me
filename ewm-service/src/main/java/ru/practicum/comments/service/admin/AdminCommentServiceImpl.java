package ru.practicum.comments.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.CommentStatus;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.NotFoundException;

@Service
@RequiredArgsConstructor
public class AdminCommentServiceImpl implements AdminCommentService {

    private final CommentRepository commentRepository;

    @Override
    public CommentDto moderateStatus(Long comId, String status) {
        Comment comment = commentRepository.findById(comId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id = " + comId + " не найден"));

        CommentStatus commentStatus;
        try {
            commentStatus = CommentStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new BadRequestException("Указан несуществущий статус комментария");
        }

        if (!commentStatus.equals(CommentStatus.PUBLISHED) || !commentStatus.equals(CommentStatus.REJECTED)) {
            throw new BadRequestException("Указан некорректный статус модерации");

        }
        comment.setStatus(commentStatus);
        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.convertToCommentDto(savedComment);
    }
}


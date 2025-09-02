package ru.practicum.comments.service.privates;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.CommentStatus;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.State;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ForbiddenException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PrivateCommentServiceImpl implements PrivateCommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public CommentDto create(NewCommentDto newCommentDto, Long eventId, Long userId) {

        User user = checkAndGetUserById(userId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с Id = " + eventId + " не найдено"));

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new BadRequestException("Событие еще не опубликовано");
        }
        Comment comment = Comment.builder()
                .text(newCommentDto.getText())
                .author(user)
                .event(event)
                .createdOn(LocalDateTime.now())
                .status(CommentStatus.PENDING)
                .build();
        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.convertToCommentDto(savedComment);
    }

    @Override
    public CommentDto update(NewCommentDto newCommentDto, Long commentId, Long userId) {
        User user = checkAndGetUserById(userId);

        Comment comment = checkAndGetCommentById(commentId);

        validateAuthor(comment, userId);

        comment.setText(newCommentDto.getText());
        comment.setStatus(CommentStatus.PENDING);

        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.convertToCommentDto(savedComment);
    }

    @Override
    public void delete(Long commentId, Long userId) {
        Comment comment = checkAndGetCommentById(commentId);

        validateAuthor(comment, userId);

        commentRepository.delete(comment);
    }

    private User checkAndGetUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
    }

    private Comment checkAndGetCommentById(Long comId) {
        return commentRepository.findById(comId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id = " + comId + " не найден"));
    }

    private void validateAuthor(Comment comment, Long userId) {
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ForbiddenException("Пользователб не является автором комментария");
        }
    }
}

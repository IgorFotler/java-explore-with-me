package ru.practicum.comments.service.publics;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.CommentStatus;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicCommentServiceImpl implements PublicCommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CommentDto> getAll(Long eventId, int from, int size) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Событие с eventId = " + eventId + " не найдено");
        }

        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by("createdOn").descending());

        List<Comment> comments = commentRepository.findAllByEventIdAndStatus(eventId, CommentStatus.PUBLISHED, pageRequest);
        return comments.stream()
                .map(CommentMapper::convertToCommentDto)
                .toList();
    }
}


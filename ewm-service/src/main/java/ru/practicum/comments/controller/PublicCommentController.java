package ru.practicum.comments.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.service.publics.PublicCommentService;

import java.util.List;

@RestController
@RequestMapping("/events/{eventId}/comments")
@Slf4j
@RequiredArgsConstructor
public class PublicCommentController {

    private final PublicCommentService publicCommentService;

    @GetMapping
    public List<CommentDto> getAll(@PathVariable @Positive Long eventId,
                                   @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                   @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Получен HTTP-запрос на получение комментариев события с d = {}", eventId);
        List<CommentDto> comments = publicCommentService.getAll(eventId, from, size);
        log.info("Получен HTTP-запрос на получение комментариев события с d = {}", eventId);
        return comments;
    }
}

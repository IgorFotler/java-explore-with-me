package ru.practicum.comments.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.service.admin.AdminCommentService;

@RestController
@RequestMapping("/admin/comments/{commentId}")
@RequiredArgsConstructor
@Slf4j
public class AdminCommentController {
    private final AdminCommentService adminCommentService;

    @PatchMapping
    public CommentDto moderateStatus(@PathVariable @Positive Long commentId,
                                     @RequestParam @NotBlank String status) {
        log.info("Получен HTTP-запрос на модерацию комментария с id = {}, CommentStatus = {}", commentId, status);
        CommentDto comment = adminCommentService.moderateStatus(commentId, status);
        log.info("Успешно обработан HTTP-запрос на модерацию комментария с id = {}, CommentStatus = {}", commentId, status);
        return comment;
    }
}

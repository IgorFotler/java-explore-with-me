package ru.practicum.comments.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.service.privates.PrivateCommentService;

@RestController
@RequestMapping("/events/comments")
@Slf4j
@RequiredArgsConstructor
public class PrivateCommentController {

    private final PrivateCommentService privateCommentService;

    @PostMapping("{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@RequestBody @Valid NewCommentDto newCommentDto,
                             @PathVariable Long eventId,
                             @RequestParam Long userId) {
        log.info("Получен HTTP-запрос на создание комментария - {}, события с id = {}, пользователем с id = {}",
                newCommentDto, eventId, userId);
        CommentDto comment = privateCommentService.create(newCommentDto, eventId, userId);
        log.info("Успешно обработан HTTP-запрос на создание комментария - {}, события с id = {}, пользователм с id = {}",
                newCommentDto, eventId, userId);
        return comment;
    }

    @PutMapping("{commentId}")
    public CommentDto update(@RequestBody @Valid NewCommentDto commentDto,
                             @PathVariable Long commentId,
                             @RequestParam Long userId) {
        log.info("Получен HTTP-запрос на обновление комментария - {}, с id = {}, пользователем с id = {}",
                commentDto, commentId, userId);
        CommentDto comment = privateCommentService.update(commentDto, commentId, userId);
        log.info("Успешно обработан HTTP-запрос на обновление комментария - {}, с id = {}, пользователем с id = {}",
                commentDto, commentId, userId);
        return comment;
    }

    @DeleteMapping("{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long commentId,
                       @RequestParam Long userId) {
        log.info("Получен HTTP-запрос на удаление комментария c id = {}, пользователем с id = {}",
                commentId, userId);
        privateCommentService.delete(commentId, userId);
        log.info("Успешно обработан HTTP-запрос на удаление комментария c id = {}, пользователем с id = {}",
                commentId, userId);
    }
}

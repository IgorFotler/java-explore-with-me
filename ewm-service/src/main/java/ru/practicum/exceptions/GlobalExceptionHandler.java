package ru.practicum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleUncaught(Exception exception) {
        return ApiError.builder().errorCode(HttpStatus.BAD_REQUEST.value()).description(exception.getMessage()).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(ConflictException de) {
        return ApiError.builder().errorCode(HttpStatus.CONFLICT.value()).description(de.getMessage()).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(NotFoundException nfe) {
        return ApiError.builder().errorCode(HttpStatus.NOT_FOUND.value()).description(nfe.getMessage()).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(BadRequestException bre) {
        return ApiError.builder().errorCode(HttpStatus.BAD_REQUEST.value()).description(bre.getMessage()).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleBadRequestException(ForbiddenException fe) {
        return ApiError.builder().errorCode(HttpStatus.BAD_REQUEST.value()).description(fe.getMessage()).build();
    }
}
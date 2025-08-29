package ru.practicum.requests.service;

import ru.practicum.requests.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    ParticipationRequestDto create(Long userId, Long eventId);

    List<ParticipationRequestDto> getUserRequests(Long userId);

    ParticipationRequestDto cancelUserRequest(Long userId, Long requestId);
}

package ru.practicum.events.service.privates;

import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventUserRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateEventService {

    EventFullDto create(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getUserEvents(Long userId, int from, int size);

    EventFullDto updateByUser(Long userId, Long eventId, UpdateEventUserRequest updateRequest);

    EventFullDto getById(Long userId, Long eventId);

    List<ParticipationRequestDto> getAllRequestsForEvents(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest);
}

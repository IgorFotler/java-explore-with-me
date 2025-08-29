package ru.practicum.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.State;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.mapper.RequestMapper;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.model.RequestStatus;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public ParticipationRequestDto create(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id = " + eventId + " не найдено"));

        validation(user, event);

        ParticipationRequest participationRequest = createNewRequest(user, event, RequestStatus.PENDING);

        if (event.getParticipantLimit() == 0) {
            participationRequest = createNewRequest(user, event, RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }

        if (!(event.getRequestModeration()) &&
                (event.getParticipantLimit() > event.getConfirmedRequests())) {
            participationRequest = createNewRequest(user, event, RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }

        eventRepository.save(event);

        return RequestMapper.convertToParticipationRequestDto(participationRequest);
    }

    private void validation(User user, Event event) {
        Long eventId = event.getId();
        Long userId = user.getId();

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Нельзя принимать участие в своем событии");
        }

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Событие не опубликовано");
        }

        if (Objects.equals(event.getParticipantLimit(), event.getConfirmedRequests()) && event.getParticipantLimit() != 0) {
            throw new ConflictException("Превышен лимит участников");
        }

        if (requestRepository.existsByEventIdAndRequesterId(eventId, userId)) {
            throw new ConflictException("Запрос уже создан");
        }
    }

    private ParticipationRequest createNewRequest(User user, Event event, RequestStatus state) {
        ParticipationRequest participationRequest = requestRepository.save(ParticipationRequest.builder()
                .requester(user)
                .event(event)
                .created(LocalDateTime.now())
                .status(state)
                .build());
        return  participationRequest;
    }


    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));

        return requestRepository.findByRequesterId(userId).stream()
                .map(RequestMapper::convertToParticipationRequestDto)
                .toList();
    }

    @Override
    public ParticipationRequestDto cancelUserRequest(Long userId, Long requestId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        ParticipationRequest participationRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id = " + requestId + " не найден"));

        if (!participationRequest.getStatus().equals(RequestStatus.PENDING)) {
            throw new ConflictException("Можно отменить только запросы со статусом ожидания");
        }
        participationRequest.setStatus(RequestStatus.CANCELED);
        return RequestMapper.convertToParticipationRequestDto(requestRepository.save(participationRequest));
    }
}

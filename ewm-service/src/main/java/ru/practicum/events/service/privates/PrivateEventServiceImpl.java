package ru.practicum.events.service.privates;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventUserRequest;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.State;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.ForbiddenException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.mapper.RequestMapper;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.model.RequestStatus;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateEventServiceImpl implements PrivateEventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;

    @Override
    public EventFullDto create(Long userId, NewEventDto newEventDto) {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));

        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Категория с id = " + userId + " не найдена"));

        validateDate(newEventDto.getEventDate());

        Event event = EventMapper.convertToEvent(newEventDto);
        event.setCategory(category);
        event.setInitiator(initiator);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(State.PENDING);

        Event savedEvent = eventRepository.save(event);

        return EventMapper.convertToEventFullDto(savedEvent);
    }

    @Override
    public List<EventShortDto> getUserEvents(Long userId, int from, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        PageRequest pageRequest = PageRequest.of(from / size, size);

        return eventRepository.findAllByInitiatorId(userId, pageRequest)
                .stream()
                .map(EventMapper::convertToEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateByUser(Long userId, Long eventId, UpdateEventUserRequest updateRequest) {
        Event event = validateId(userId, eventId);

        if (updateRequest.getEventDate() != null) {
            validateDate(updateRequest.getEventDate());
        }

        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Изменять можно только события, которые отменены или ожидают модерации");
        }

        if (updateRequest.getStateAction() != null) {
            switch (updateRequest.getStateAction()) {
                case CANCEL_REVIEW -> event.setState(State.CANCELED);
                case SEND_TO_REVIEW -> event.setState(State.PENDING);
            }
        }

        if (updateRequest.getCategoryId() != null) {
            Category category = categoryRepository.findById(updateRequest.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Категория с id = " + updateRequest.getCategoryId() + " не найдена"));
            event.setCategory(category);
        }

        Event updatedEvent = eventRepository.save(event);

        return EventMapper.convertToEventFullDto(updatedEvent);
    }

    @Override
    public EventFullDto getById(Long userId, Long eventId) {
        Event event = validateId(userId, eventId);

        return EventMapper.convertToEventFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getAllRequestsForEvents(Long userId, Long eventId) {
        Event event = validateId(userId, eventId);

        return requestRepository.findAllByEventId(eventId).stream()
                .map(RequestMapper::convertToParticipationRequestDto)
                .toList();
    }

    @Override
    public EventRequestStatusUpdateResult updateRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest) {
        Event event = validateId(userId, eventId);

        if (event.getState().equals(State.CANCELED)) {
            throw new ConflictException("Событие отменено");
        }

        List<ParticipationRequest> requests = requestRepository.findAllById(updateRequest.getRequestIds());

        for (ParticipationRequest request : requests) {
            if (request.getStatus() != RequestStatus.PENDING) {
                throw new ConflictException("Обрабатывать запросы можно только в статусе PENDING");
            }

            if (!request.getEvent().getId().equals(eventId)) {
                throw new ConflictException("eventId и id события не совпадают");
            }
        }

        if (Objects.equals(event.getParticipantLimit(), event.getConfirmedRequests())) {
            throw new ConflictException("Нет свободных мест для участия в событии.");
        }

        RequestStatus status = updateRequest.getStatus();

        List<ParticipationRequest> confirmedRequests = new ArrayList<>();
        List<ParticipationRequest> rejectedRequests = new ArrayList<>();

        int limit = event.getParticipantLimit();
        long confirmed = event.getConfirmedRequests();

        if (status.equals(RequestStatus.CONFIRMED)) {
            for (ParticipationRequest request : requests) {
                if (limit > confirmed) {
                    request.setStatus(RequestStatus.CONFIRMED);
                    confirmedRequests.add(request);
                    confirmed++;
                } else {
                    request.setStatus(RequestStatus.REJECTED);
                    rejectedRequests.add(request);
                }
            }

            event.setConfirmedRequests(event.getConfirmedRequests() + confirmedRequests.size());
            eventRepository.save(event);

        } else if (status.equals(RequestStatus.REJECTED)) {
            for (ParticipationRequest request : requests) {
                request.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(request);
            }
        }
        requestRepository.saveAll(requests);
        return new EventRequestStatusUpdateResult(
                confirmedRequests.stream().map(RequestMapper::convertToParticipationRequestDto).toList(),
                rejectedRequests.stream().map(RequestMapper::convertToParticipationRequestDto).toList()
        );
    }

    private Event validateId(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id = " + eventId + " не найден"));

        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ForbiddenException("Данный пользователь не является создателем события");
        }
        return event;
    }

    private void validateDate(LocalDateTime date) {
        if (date.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Событие должно начинаться не раньше чем через два часа после создания");
        }
    }
}

package ru.practicum.events.service.admin;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.State;
import ru.practicum.events.model.StateAction;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository eventRepository;

    @Override
    public List<EventFullDto> searchEvents(
            List<Long> users,
            List<String> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            int from,
            int size,
            HttpServletRequest request) {

        PageRequest page = PageRequest.of(from / size, size);

        List<State> eventStates = null;
        if (states != null) {
            eventStates = states.stream()
                    .map(State::valueOf)
                    .toList();
        }

        List<Event> events = eventRepository.findEventsByAdminFilters(
                users,
                eventStates,
                categories,
                rangeStart,
                rangeEnd,
                page
        );

        return events.stream()
                .map(EventMapper::convertToEventFullDto)
                .toList();
    }

    @Override
    public EventFullDto updateByAdmin(Long eventId, UpdateEventAdminRequest updateRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id = " + eventId + " не найдено"));

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ConflictException("До начала события осталось меньше часа");
        }

        if ((event.getState().equals(State.CANCELED) || (event.getState().equals(State.PUBLISHED)))) {
            throw new ConflictException("Нельзя опубликовать событие, которое отменено или уже опубликовано");
        }

        if (updateRequest.getStateAction() != null) {
            StateAction updateState = updateRequest.getStateAction();

            switch (updateState) {
                case PUBLISH_EVENT -> {
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                }
                case REJECT_EVENT -> event.setState(State.CANCELED);
                default -> throw new ConflictException("Некорректный статус");
            }
        }

        Event updatedEvent = eventRepository.save(event);
        return EventMapper.convertToEventFullDto(updatedEvent);
    }
}

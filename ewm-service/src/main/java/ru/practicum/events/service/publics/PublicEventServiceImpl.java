package ru.practicum.events.service.publics;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.State;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.requests.model.RequestStatus;
import ru.practicum.requests.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {

    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final StatsClient statsClient;
    private final String appName = "ewm-service";

    @Override
    public List<EventShortDto> getAll(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            String sort,
            int from,
            int size,
            HttpServletRequest request) {

        saveHit(request);

        if (rangeStart != null && rangeEnd != null && rangeEnd.isBefore(rangeStart)) {
            throw new BadRequestException("Конец диапазона не может быть раньше начала");
        }

        LocalDateTime start = (rangeStart != null) ? rangeStart : LocalDateTime.now();
        LocalDateTime end = (rangeEnd != null) ? rangeEnd : LocalDateTime.now().plusYears(100);

        PageRequest pageRequest;
        if ("EVENT_DATE".equalsIgnoreCase(sort)) {
            pageRequest = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "eventDate"));
        } else {
            pageRequest = PageRequest.of(from / size, size);
        }

        Page<Event> eventsPage = eventRepository.findPublicEvents(
                (text == null || text.isBlank()) ? null : text,
                (categories == null || categories.isEmpty()) ? null : categories,
                paid,
                start,
                end,
                onlyAvailable != null ? onlyAvailable : false,
                pageRequest
        );

        List<Event> events = eventsPage.getContent();

        List<String> uris = events.stream().map(e -> "/events/" + e.getId()).toList();
        Map<String, Long> viewsMap = getViews(uris, start, end, false);

        Map<Long, Long> confirmedMap = getConfirmed(events);

        List<EventShortDto> results = events.stream()
                .map(e -> {
                    Long views = viewsMap.getOrDefault("/events/" + e.getId(), 0L);
                    Long confirmed = confirmedMap.getOrDefault(e.getId(), 0L);
                    return EventMapper.convertToEventShortDto(e, views, confirmed);
                })
                .toList();

        if ("VIEWS".equalsIgnoreCase(sort)) {
            results = results.stream()
                    .sorted(Comparator.comparingLong(EventShortDto::getViews).reversed()
                            .thenComparing(EventShortDto::getEventDate))
                    .toList();
        }

        return results;
    }

    @Override
    public EventFullDto getById(Long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Событие с id = " + id + " не найдено"));

        if (event.getState() != State.PUBLISHED) {
            throw new NotFoundException("Событие еще не опубликовано");
        }
        saveHit(request);

        LocalDateTime start = (event.getPublishedOn() != null ? event.getPublishedOn() : event.getCreatedOn())
                .minusYears(100);
        LocalDateTime end = LocalDateTime.now();
        String uri = "/events/" + event.getId();

        Long views = getViews(List.of(uri), start, end, true).getOrDefault(uri, 0L);
        Long confirmed = requestRepository.requestsCountByEventAndStatusId(event.getId(), RequestStatus.CONFIRMED);
        return EventMapper.convertToEventFullDto(event, views, confirmed);
    }

    private void saveHit(HttpServletRequest request) {
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
           .app(appName)
           .uri(request.getRequestURI())
           .ip(request.getRemoteAddr())
           .timestamp(LocalDateTime.now())
           .build();
        statsClient.create(endpointHitDto);
    }

    private Map<String, Long> getViews(Collection<String> uris, LocalDateTime start, LocalDateTime end, boolean unique) {
        try {
            List<ViewStatsDto> stats = (List<ViewStatsDto>) statsClient.getStats(start, end, new ArrayList<>(uris), unique);
            Map<String, Long> map = new HashMap<>();
            for (ViewStatsDto s : stats) {
                map.merge(s.getUri(), s.getHits(), Long::sum);
            }
            return map;
        } catch (Exception exception) {
            return Collections.emptyMap();
        }
    }

    private Map<Long, Long> getConfirmed(List<Event> events) {
        List<Long> eventIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        return requestRepository.countConfirmedRequestsByEventIds(eventIds, RequestStatus.CONFIRMED);
    }
}

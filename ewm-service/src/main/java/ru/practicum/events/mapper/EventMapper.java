package ru.practicum.events.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.model.Event;
import ru.practicum.users.mapper.UserMapper;

import java.time.format.DateTimeFormatter;

@Component
public class EventMapper {

    public static EventShortDto convertToEventShortDto(Event event) {
        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.convertToCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                UserMapper.convertToUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getViews()
        );
    }

    public static EventShortDto convertToEventShortDto(Event event, Long views, Long confirmedRequests) {
        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.convertToCategoryDto(event.getCategory()),
                confirmedRequests,
                event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                UserMapper.convertToUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                views
        );
    }

    public static EventFullDto convertToEventFullDto(Event event) {
        return new EventFullDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.convertToCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                UserMapper.convertToUserShortDto(event.getInitiator()),
                LocationMapper.toLocationDto(event.getLocation()),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews()
        );
    }

    public static EventFullDto convertToEventFullDto(Event event, Long views, Long confirmedRequests) {
        return new EventFullDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.convertToCategoryDto(event.getCategory()),
                confirmedRequests,
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                UserMapper.convertToUserShortDto(event.getInitiator()),
                LocationMapper.toLocationDto(event.getLocation()),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                views
        );
    }

    public static Event convertToEvent(NewEventDto newEventDto) {
        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        event.setPaid(newEventDto.getPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.getRequestModeration());
        event.setTitle(newEventDto.getTitle());
        return event;
    }
}

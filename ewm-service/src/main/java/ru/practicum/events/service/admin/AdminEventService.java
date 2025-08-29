package ru.practicum.events.service.admin;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {

    List<EventFullDto> searchEvents(
            List<Long> users,
            List<String> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            int from,
            int size,
            HttpServletRequest request);

    EventFullDto updateByAdmin(Long eventId, UpdateEventAdminRequest updateRequest);
}

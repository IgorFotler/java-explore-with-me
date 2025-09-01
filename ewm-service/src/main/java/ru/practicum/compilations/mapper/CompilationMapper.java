package ru.practicum.compilations.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.events.model.Event;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.mapper.EventMapper;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CompilationMapper {

    public static Compilation convertToCompilation(NewCompilationDto newCompilationDto, Set<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setTitle(newCompilationDto.getTitle());
        compilation.setPinned(newCompilationDto.getPinned());
        compilation.setEvents(events);
        return compilation;
    }

    public static CompilationDto convertToCompilationDto(Compilation compilation) {
        Set<EventShortDto> events = compilation.getEvents().stream()
                .map(EventMapper::convertToEventShortDto)
                .collect(Collectors.toSet());

        return new CompilationDto(compilation.getId(), compilation.getPinned(), compilation.getTitle(), events);
    }
}

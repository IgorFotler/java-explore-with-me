package ru.practicum.compilations.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationRequest;
import ru.practicum.compilations.mapper.CompilationMapper;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.NotFoundException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminCompilationServiceImpl implements AdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        Set<Event> events = newCompilationDto.getEvents() != null ?
                eventRepository.findAllByIdIn(newCompilationDto.getEvents()) :
                Collections.emptySet();

        Compilation compilation = CompilationMapper.convertToCompilation(newCompilationDto, events);
        return CompilationMapper.convertToCompilationDto(compilationRepository.save(compilation));

    }

    @Override
    public void delete(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("Подборка с id = " + compId + " не найдена");
        }

        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка c id = %d, не найдена!".formatted(compId)));

        if (updateCompilationRequest.getEvents() != null && !updateCompilationRequest.getEvents().isEmpty()) {
            Set<Event> events = new HashSet<>(eventRepository.findAllById(updateCompilationRequest.getEvents()));
            compilation.setEvents(events);
        }
        compilationRepository.save(compilation);
        return CompilationMapper.convertToCompilationDto(compilation);
    }
}

package ru.practicum.compilations.service.admin;

import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationRequest;

public interface AdminCompilationService {

    CompilationDto create(NewCompilationDto newCompilationDto);

    void delete(Long compId);

    CompilationDto update(Long compId, UpdateCompilationRequest updateRequest);

}

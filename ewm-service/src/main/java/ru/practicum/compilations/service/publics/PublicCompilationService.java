package ru.practicum.compilations.service.publics;

import ru.practicum.compilations.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationService {

    List<CompilationDto> findAll(Boolean pinned, int from, int size);

    CompilationDto findById(Long compId);
}

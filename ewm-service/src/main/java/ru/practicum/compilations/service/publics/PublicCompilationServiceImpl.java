package ru.practicum.compilations.service.publics;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.mapper.CompilationMapper;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCompilationServiceImpl implements PublicCompilationService {

    private final CompilationRepository compilationRepository;

    @Override
    public List<CompilationDto> findAll(Boolean pinned, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);

        List<Compilation> compilations = pinned != null ?
                compilationRepository.findAllByPinned(pinned, pageRequest) :
                compilationRepository.findAll(pageRequest).getContent();

        return compilations.stream()
                .map(CompilationMapper::convertToCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto findById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка с id = " + compId + " не найдена"));
        return CompilationMapper.convertToCompilationDto(compilation);
    }
}

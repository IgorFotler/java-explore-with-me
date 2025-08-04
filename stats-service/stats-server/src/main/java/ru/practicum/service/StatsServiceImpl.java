package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.mapper.ViewStatsMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final EndpointHitMapper endpointHitMapper;
    private final ViewStatsMapper viewStatsMapper;

    @Override
    public EndpointHitDto create(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = endpointHitMapper.convertToEntity(endpointHitDto);
        endpointHit = statsRepository.save(endpointHit);
        return endpointHitMapper.convertToDto(endpointHit);
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start.isAfter(end)) {
            throw new ValidationException("Значение start не может быть позже end");
        }
        if (unique) {
            return statsRepository.findUniqueStats(start, end, uris).stream()
                    .map(viewStatsMapper::convertToDto)
                    .toList();
        } else {
            return statsRepository.findAllStats(start, end, uris).stream()
                    .map(viewStatsMapper::convertToDto)
                    .toList();
        }
    }
}


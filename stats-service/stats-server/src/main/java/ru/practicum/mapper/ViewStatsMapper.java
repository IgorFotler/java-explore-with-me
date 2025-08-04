package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.ViewStats;

@Component
public class ViewStatsMapper {

    public ViewStatsDto convertToDto(ViewStats viewStats) {
        return new ViewStatsDto(viewStats.getApp(), viewStats.getUri(), viewStats.getHits());
    }

    public ViewStats convertToEntity(ViewStatsDto viewStatsDto) {
        return new ViewStats(viewStatsDto.getApp(), viewStatsDto.getUri(), viewStatsDto.getHits());
    }
}

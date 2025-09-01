package ru.practicum.events.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.events.dto.LocationDto;
import ru.practicum.events.model.Location;

@Component
public class LocationMapper {

    public static LocationDto toLocationDto(Location location) {
        return new LocationDto(location.getLat(), location.getLon());
    }

    public static Location toLocation(LocationDto locationDto) {
        return new Location(locationDto.getLat(), locationDto.getLon());
    }
}

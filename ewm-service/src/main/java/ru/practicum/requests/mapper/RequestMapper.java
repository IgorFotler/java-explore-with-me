package ru.practicum.requests.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.model.ParticipationRequest;

@Component
public class RequestMapper {

    public static ParticipationRequestDto convertToParticipationRequestDto(ParticipationRequest participationRequest) {
        return new ParticipationRequestDto(
                participationRequest.getId(),
                participationRequest.getEvent().getId(),
                participationRequest.getRequester().getId(),
                participationRequest.getCreated(),
                participationRequest.getStatus()
        );
    }
}

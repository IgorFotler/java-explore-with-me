package ru.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewStatsDto {

    @NotBlank(message = "Поле app не может быть пустым")
    private String app;

    @NotBlank(message = "Поле uri не может быть пустым")
    private String uri;

    @NotBlank(message = "Поле hits не может быть пустым")
    private Long hits;
}

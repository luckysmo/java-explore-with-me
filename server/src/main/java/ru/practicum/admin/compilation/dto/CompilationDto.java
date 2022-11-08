package ru.practicum.admin.compilation.dto;

import lombok.Data;
import ru.practicum.priv.event.dto.EventFullDto;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class CompilationDto {
    private Long id;
    @NotBlank
    private String title;
    private boolean pinned;
    private List<EventFullDto> events;
}

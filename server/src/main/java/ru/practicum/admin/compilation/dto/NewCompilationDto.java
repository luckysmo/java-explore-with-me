package ru.practicum.admin.compilation.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class NewCompilationDto {
    private Long id;
    @NotBlank
    private String title;
    private boolean pinned;
    private List<Long> events;
}

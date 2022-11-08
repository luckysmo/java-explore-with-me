package ru.practicum.publics.compilation.service;

import ru.practicum.admin.compilation.dto.CompilationDto;

import java.util.List;

public interface CompilationPublicService {
    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilationById(Long id);
}

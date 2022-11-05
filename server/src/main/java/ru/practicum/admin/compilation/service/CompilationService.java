package ru.practicum.admin.compilation.service;

import ru.practicum.admin.compilation.dto.CompilationDto;
import ru.practicum.admin.compilation.dto.NewCompilationDto;

public interface CompilationService {
    CompilationDto saveCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(Long id);

    void deleteEventFromCompilation(Long compId, Long eventId);

    void addEventToCompilation(Long compId, Long eventId);

    void compilationPinnedOff(Long id);

    void compilationPinnedOn(Long id);
}

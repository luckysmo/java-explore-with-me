package ru.practicum.admin.compilation.dto;

import ru.practicum.admin.compilation.Compilation;
import ru.practicum.priv.event.dto.EventMapper;

import java.util.ArrayList;
import java.util.List;

public class CompilationMapper {
    public static CompilationDto compilationToDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setPinned(compilation.isPinned());
        if (compilation.getEvents() != null) {
            compilationDto.setEvents(EventMapper.eventToEventFullDto(compilation.getEvents()));
        }
        return compilationDto;
    }

    public static List<CompilationDto> compilationToDto(Iterable<Compilation> compilations) {
        List<CompilationDto> dtos = new ArrayList<>();
        compilations.forEach(x -> dtos.add(compilationToDto(x)));

        return dtos;
    }

    public static Compilation compilationFromDto(NewCompilationDto compilationDto) {
        Compilation compilation = new Compilation();
        compilation.setTitle(compilationDto.getTitle());
        compilation.setPinned(compilationDto.isPinned());

        return compilation;
    }
}

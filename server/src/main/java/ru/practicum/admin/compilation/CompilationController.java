package ru.practicum.admin.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.admin.compilation.dto.CompilationDto;
import ru.practicum.admin.compilation.dto.NewCompilationDto;
import ru.practicum.admin.compilation.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class CompilationController {
    private final CompilationService compilationService;

    @PostMapping
    public CompilationDto saveCompilation(@Valid @RequestBody NewCompilationDto compilationDto) {
        return compilationService.saveCompilation(compilationDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable Long compId,
                                           @PathVariable Long eventId) {
        compilationService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventFromCompilation(@PathVariable Long compId,
                                        @PathVariable Long eventId) {
        compilationService.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void deleteCompilationFromDesk(@PathVariable Long compId) {
        compilationService.compilationPinnedOff(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void addCompilationToDesk(@PathVariable Long compId) {
        compilationService.compilationPinnedOn(compId);
    }
}

package ru.practicum.publics.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.admin.compilation.dto.CompilationDto;
import ru.practicum.publics.compilation.service.CompilationPublicService;

import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
public class CompilationPublicController {
    private final CompilationPublicService compilationPublicService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0", required = false) int from,
                                                @RequestParam(defaultValue = "10", required = false) int size) {
        return compilationPublicService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        return compilationPublicService.getCompilationById(compId);
    }
}

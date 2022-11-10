package ru.practicum.admin.event.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.admin.compilation.Compilation;
import ru.practicum.exceptions.CompilationNotFoundException;

public interface CompilationRepository extends JpaRepository<Compilation, Long>, CompilationRepositoryCustom {
    default Compilation checkAndReturnCompilationIfExist(Long id) {
        return findById(id).orElseThrow(
                () -> new CompilationNotFoundException(String.format("Компиляция с id=%s не найдена", id)));
    }

    Compilation findCompilationById(Long id);
}

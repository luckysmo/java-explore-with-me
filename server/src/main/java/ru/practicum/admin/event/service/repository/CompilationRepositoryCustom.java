package ru.practicum.admin.event.service.repository;

import ru.practicum.admin.compilation.Compilation;

import java.util.List;

public interface CompilationRepositoryCustom {
    List<Compilation> findCompilationsByParam(Boolean pinned, int from, int size);
}

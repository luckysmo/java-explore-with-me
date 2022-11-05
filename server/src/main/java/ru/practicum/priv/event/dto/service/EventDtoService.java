package ru.practicum.priv.event.dto.service;

import ru.practicum.priv.event.dto.EventShortDto;

import java.util.List;

public interface EventDtoService {
    <T extends EventShortDto> T fillAdditionalInfo(T eventDto);

    <T extends EventShortDto> List<T> fillAdditionalInfo(List<T> eventDto);
}

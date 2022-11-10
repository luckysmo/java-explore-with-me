package ru.practicum.publics.event.service;

import ru.practicum.priv.event.dto.EventFullDto;
import ru.practicum.priv.event.dto.EventShortDto;
import ru.practicum.publics.event.EventKindSort;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventPublicService {
    List<EventShortDto> getEvents(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            EventKindSort sort,
            int from,
            int size);

    EventFullDto getEvent(Long id);

    EventFullDto saveStat(EventFullDto eventShortDto, HttpServletRequest request);

    List<EventShortDto> saveStatList(List<EventShortDto> dtoCollection, HttpServletRequest request);

    List<EventFullDto> getRatingEvent(String sort);
}

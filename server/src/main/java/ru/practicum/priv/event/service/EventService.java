package ru.practicum.priv.event.service;

import ru.practicum.priv.event.dto.EventFullDto;
import ru.practicum.priv.event.dto.NewEventDto;
import ru.practicum.priv.event.dto.UpdateEventDto;
import ru.practicum.priv.request.dto.RequestDto;

import java.util.List;

public interface EventService {
    EventFullDto saveEvent(NewEventDto newEventDto, Long userId);

    EventFullDto updateEvent(UpdateEventDto updateEventDto, Long userId);

    List<EventFullDto> getEvents(Long userId, int from, int size);

    EventFullDto getEvent(Long userId, Long eventId);

    EventFullDto cancelEvent(Long userId, Long eventId);

    List<RequestDto> getEventRequests(Long userId, Long eventId);

    RequestDto confirmEventRequest(Long userId, Long eventId, Long reqId);

    RequestDto rejectEventRequest(Long userId, Long eventId, Long reqId);
}

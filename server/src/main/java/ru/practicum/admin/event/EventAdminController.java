package ru.practicum.admin.event;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.admin.event.service.EventAdminService;
import ru.practicum.exceptions.EventBadRequestException;
import ru.practicum.priv.event.EventState;
import ru.practicum.priv.event.dto.EventFullDto;
import ru.practicum.priv.event.dto.NewEventDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class EventAdminController {
    private final EventAdminService eventAdminService;

    @GetMapping
    public Iterable<EventFullDto> getEvents(@RequestParam List<Long> users,
                                            @RequestParam List<EventState> states,
                                            @RequestParam List<Long> categories,
                                            @RequestParam String rangeStart,
                                            @RequestParam String rangeEnd,
                                            @RequestParam(defaultValue = "0", required = false) int from,
                                            @RequestParam(defaultValue = "10", required = false) int size) {
        LocalDateTime rangeStartDate;
        LocalDateTime rangeEndDate;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            rangeStartDate = LocalDateTime.parse(rangeStart, formatter);
            rangeEndDate = LocalDateTime.parse(rangeEnd, formatter);
        } catch (DateTimeParseException e) {
            throw new EventBadRequestException(
                    String.format("Неверный формат дат rangeStart %s или rangeEnd %s", rangeStart, rangeEnd));
        }
        return eventAdminService.getEvents(
                users,
                states,
                categories,
                rangeStartDate,
                rangeEndDate,
                from,
                size);
    }

    @PutMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @RequestBody NewEventDto eventDto) {
        return eventAdminService.updateEvent(eventId, eventDto);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable Long eventId) {
        return eventAdminService.publishEvent(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable Long eventId) {
        return eventAdminService.rejectEvent(eventId);
    }
}

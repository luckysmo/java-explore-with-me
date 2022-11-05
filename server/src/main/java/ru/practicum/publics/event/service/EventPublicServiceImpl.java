package ru.practicum.publics.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.EventForbiddenException;
import ru.practicum.priv.event.Event;
import ru.practicum.priv.event.EventState;
import ru.practicum.priv.event.dto.EventFullDto;
import ru.practicum.priv.event.dto.EventMapper;
import ru.practicum.priv.event.dto.EventShortDto;
import ru.practicum.priv.event.dto.service.EventDtoService;
import ru.practicum.priv.event.repository.EventRepository;
import ru.practicum.publics.event.EventKindSort;
import ru.practicum.stat.EventStatDto;
import ru.practicum.stat.StatClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventPublicServiceImpl implements EventPublicService {
    private final EventRepository eventRepository;
    private final StatClient statClient;

    private final EventDtoService eventDtoService;

    @Override
    public List<EventShortDto> getEvents(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            EventKindSort sort,
            int from,
            int size) {
        List<Event> events = eventRepository.findEventsByParam(
                text,
                categories,
                paid,
                rangeStart,
                rangeEnd);
        List<EventShortDto> eventShortDtos = eventDtoService.fillAdditionalInfo(EventMapper.eventToEventShortDto(events));

        return sortEventList(eventShortDtos, sort, from, size);
    }

    @Override
    public EventFullDto getEvent(Long id) {
        log.debug("Запрос getEvent по id - {}", id);
        Event event = eventRepository.checkAndReturnEventIfExist(id);
        if (event.getState() != EventState.PUBLISHED) {
            throw new EventForbiddenException(
                    String.format("Попытка получения не опубликованного события по id %s", id));
        }
        return eventDtoService.fillAdditionalInfo(EventMapper.eventToEventFullDto(event));
    }

    @Override
    public EventFullDto saveStat(EventFullDto eventShortDto, HttpServletRequest request) {
        saveEventStat(request.getRequestURI(), request.getRemoteAddr());
        return eventShortDto;
    }

    @Override
    public List<EventShortDto> saveStatList(List<EventShortDto> dtoCollection, HttpServletRequest request) {
        dtoCollection.forEach(x -> saveEventStat(request.getRequestURI() + "/" + x.getId(), request.getRemoteAddr()));
        return dtoCollection;
    }

    private void saveEventStat(String uri, String ip) {
        EventStatDto eventStatDto = new EventStatDto();
        eventStatDto.setApp(StatClient.APP_NAME);
        eventStatDto.setUri(uri);
        eventStatDto.setIp(ip);
        eventStatDto.setDateHit(LocalDateTime.now());

        statClient.save(eventStatDto);
    }

    private List<EventShortDto> sortEventList(
            List<EventShortDto> eventList,
            EventKindSort sort,
            int from,
            int size) {
        if (sort == EventKindSort.VIEWS) {
            eventList = eventList.stream()
                    .sorted(Comparator.comparing(EventShortDto::getViews).reversed())
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());
        } else {
            eventList = eventList.stream()
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());
        }
        return eventList;
    }
}

package ru.practicum.admin.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.admin.category.repository.CategoryRepository;
import ru.practicum.exceptions.EventForbiddenException;
import ru.practicum.priv.event.Event;
import ru.practicum.priv.event.EventState;
import ru.practicum.priv.event.dto.EventFullDto;
import ru.practicum.priv.event.dto.NewEventDto;
import ru.practicum.priv.event.dto.service.EventDtoService;
import ru.practicum.priv.event.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.priv.event.dto.EventMapper.eventToEventFullDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventAdminServiceImpl implements EventAdminService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final EventDtoService eventDtoService;

    @Override
    public List<EventFullDto> getEvents(List<Long> users,
                                        List<EventState> states,
                                        List<Long> categories,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        int from,
                                        int size) {
        return eventDtoService.fillAdditionalInfo(eventToEventFullDto(
                eventRepository.findEventsByParam(users, states, categories, rangeStart, rangeEnd, from, size)));
    }

    @Override
    public EventFullDto updateEvent(Long id, NewEventDto eventDto) {
        log.debug("Запрос updateEvent по id - {}", id);

        Event event = eventRepository.checkAndReturnEventIfExist(id);

        eventDto.setFieldsToEvent(event);
        if (eventDto.getCategory() != null) {
            event.setCategory(categoryRepository.checkAndReturnCategoryIfExist(eventDto.getCategory()));
        }
        return eventDtoService.fillAdditionalInfo(
                eventToEventFullDto(eventRepository.save(event)));
    }

    @Override
    public EventFullDto publishEvent(Long id) {
        log.debug("Запрос publishEvent по id - {}", id);

        Event event = eventRepository.checkAndReturnEventIfExist(id);

        LocalDateTime datePublish = LocalDateTime.now();

        if (event.getEventDate().isBefore(datePublish.plusHours(1))) {
            throw new EventForbiddenException("Время публикации события вышло");
        }
        if (event.getState() != EventState.PENDING) {
            throw new EventForbiddenException(
                    String.format("Событие должно быть в статусе PENDING, но имеет статус %s", event.getState()));
        }
        event.setPublishedOn(datePublish);
        event.setState(EventState.PUBLISHED);

        EventFullDto eventFullDto = eventToEventFullDto(eventRepository.save(event));

        return eventDtoService.fillAdditionalInfo(eventFullDto);
    }

    @Override
    public EventFullDto rejectEvent(Long id) {
        log.debug("Запрос rejectEvent по id - {}", id);

        Event event = eventRepository.checkAndReturnEventIfExist(id);
        if (event.getState() == EventState.PUBLISHED) {
            throw new EventForbiddenException("Событие уже опубликовано");
        }
        event.setState(EventState.CANCELED);

        return eventDtoService.fillAdditionalInfo(eventToEventFullDto(eventRepository.save(event)));
    }
}

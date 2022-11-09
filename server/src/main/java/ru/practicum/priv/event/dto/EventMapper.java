package ru.practicum.priv.event.dto;

import ru.practicum.priv.event.Event;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.admin.category.dto.CategoryMapper.toCategoryDto;
import static ru.practicum.admin.user.dto.UserMapper.toUserShortDto;

public class EventMapper {
    public static Event newEventDtoToEvent(NewEventDto eventDto) {
        Event event = updateEventDtoToEvent(eventDto);
        event.setId(null);
        event.setRequestModeration(eventDto.getRequestModeration());
        Location location = eventDto.getLocation();
        if (location != null) {
            event.setLatitude(location.getLat());
            event.setLongitude(location.getLon());
        }
        return event;
    }

    public static Event updateEventDtoToEvent(UpdateEventDto eventDto) {
        Event event = new Event();
        event.setId(eventDto.getEventId());
        event.setAnnotation(eventDto.getAnnotation());
        event.setDescription(eventDto.getDescription());
        event.setTitle(eventDto.getTitle());
        event.setEventDate(eventDto.getEventDate());
        event.setPaid(eventDto.getPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit());
        event.setLikeCount(eventDto.getLikeCount());
        event.setDislikeCount(eventDto.getDislikeCount());

        return event;
    }

    public static EventFullDto eventToEventFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setCategory(toCategoryDto(event.getCategory()));
        eventFullDto.setPaid(event.isPaid());
        eventFullDto.setRequestModeration(event.isRequestModeration());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setInitiator(toUserShortDto(event.getInitiator()));
        eventFullDto.setState(event.getState());
        eventFullDto.setDislikeCount(event.getDislikeCount());
        eventFullDto.setLikeCount(event.getLikeCount());
        eventFullDto.setLocation(new Location(event.getLongitude(), event.getLatitude()));

        return eventFullDto;
    }

    public static List<EventFullDto> eventToEventFullDto(Iterable<Event> events) {
        List<EventFullDto> dtos = new ArrayList<>();
        events.forEach(x -> dtos.add(eventToEventFullDto(x)));

        return dtos;
    }

    public static EventShortDto eventToEventShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setDescription(event.getDescription());
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setCategory(toCategoryDto(event.getCategory()));
        eventShortDto.setPaid(event.isPaid());
        eventShortDto.setInitiator(toUserShortDto(event.getInitiator()));
        eventShortDto.setLikeCount(event.getLikeCount());
        eventShortDto.setDislikeCount(event.getDislikeCount());

        return eventShortDto;
    }

    public static List<EventShortDto> eventToEventShortDto(Iterable<Event> events) {
        List<EventShortDto> dtos = new ArrayList<>();
        events.forEach(x -> dtos.add(eventToEventShortDto(x)));

        return dtos;
    }
}

package ru.practicum.dto;

import ru.practicum.EventStat;

public class EventStatMapper {
    public static EventStat eventStatFromDto(EventStatDto eventStatDto) {
        EventStat eventStat = new EventStat();
        eventStat.setApp(eventStatDto.getApp());
        eventStat.setUri(eventStatDto.getUri());
        eventStat.setIp(eventStatDto.getIp());
        eventStat.setDateHit(eventStatDto.getTimestamp());

        return eventStat;
    }

}

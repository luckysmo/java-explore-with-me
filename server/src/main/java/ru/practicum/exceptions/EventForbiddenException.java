package ru.practicum.exceptions;

public class EventForbiddenException extends RuntimeException {
    public EventForbiddenException(String message) {
        super(message);
    }
}

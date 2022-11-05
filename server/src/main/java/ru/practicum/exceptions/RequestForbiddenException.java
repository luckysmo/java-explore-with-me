package ru.practicum.exceptions;

public class RequestForbiddenException extends RuntimeException {
    public RequestForbiddenException(String message) {
        super(message);
    }
}

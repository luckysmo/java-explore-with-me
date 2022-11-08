package ru.practicum.exceptions;

public class CompilationBadRequestException extends RuntimeException {
    public CompilationBadRequestException(String message) {
        super(message);
    }
}

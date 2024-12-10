package ru.practicum.enw.exceptions;

public class ForbiddenCustomException extends RuntimeException {
    public ForbiddenCustomException(String message) {
        super(message);
    }
}

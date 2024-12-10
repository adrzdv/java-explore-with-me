package ru.practicum.enw.exceptions;

public class BadRequestCustomException extends RuntimeException {
    public BadRequestCustomException(String message) {

        super(message);

    }
}

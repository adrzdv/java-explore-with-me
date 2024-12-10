package ru.practicum.enw.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Objects;

@RestControllerAdvice
public class ErrorHandler {

    private static final String BAD_REQUEST = "Incorrectly made request.";
    private static final String NOT_FOUND = "The required object was not found.";
    private static final String FORBIDDEN = "For the requested operation the conditions are not met.";
    private static final String CONFLICT = "Integrity constraint has been violated.";

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleValidationExceptions(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorCustomException(HttpStatus.CONFLICT.name(),
                Objects.requireNonNull(e.getMostSpecificCause()).getMessage(),
                CONFLICT,
                LocalDateTime.now()));
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<Object> conversionalFaildException(ConversionFailedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorCustomException(HttpStatus.BAD_REQUEST.name(),
                Objects.requireNonNull(e.getCause()).getMessage(),
                BAD_REQUEST,
                LocalDateTime.now()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorCustomException(HttpStatus.BAD_REQUEST.name(),
                Objects.requireNonNull(e.getMessage()),
                BAD_REQUEST,
                LocalDateTime.now()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorCustomException(HttpStatus.BAD_REQUEST.name(),
                "Field: " + e.getFieldError().getField() + ". " + e.getBindingResult().getAllErrors().getFirst().getDefaultMessage(),
                BAD_REQUEST,
                LocalDateTime.now()));
    }


    @ExceptionHandler(NotFoundCustomException.class)
    public ResponseEntity<Object> notFoundData(NotFoundCustomException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorCustomException(HttpStatus.NOT_FOUND.name(),
                e.getMessage(),
                NOT_FOUND,
                LocalDateTime.now()));
    }

    @ExceptionHandler(BadRequestCustomException.class)
    public ResponseEntity<Object> badRequestCustomException(BadRequestCustomException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorCustomException(HttpStatus.BAD_REQUEST.name(),
                e.getMessage(),
                BAD_REQUEST,
                LocalDateTime.now()));
    }

    @ExceptionHandler(ForbiddenCustomException.class)
    public ResponseEntity<Object> forbiddenCustomException(ForbiddenCustomException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorCustomException(HttpStatus.FORBIDDEN.name(),
                e.getMessage(),
                FORBIDDEN,
                LocalDateTime.now()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> constraintViolationException(ConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorCustomException(HttpStatus.CONFLICT.name(),
                e.getMessage(),
                CONFLICT,
                LocalDateTime.now()));
    }

    @ExceptionHandler(ConflictCustomException.class)
    public ResponseEntity<Object> conflictCustomException(ConflictCustomException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorCustomException(HttpStatus.BAD_REQUEST.name(),
                e.getMessage(),
                BAD_REQUEST,
                LocalDateTime.now()));
    }


}

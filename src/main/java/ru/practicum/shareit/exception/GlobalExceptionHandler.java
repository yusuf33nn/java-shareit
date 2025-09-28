package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorMessage> handleDuplicateEmail(DuplicateEmailException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorMessage.builder()
                        .httpCode(HttpStatus.CONFLICT.value())
                        .errorMessage(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorMessage.builder()
                        .httpCode(HttpStatus.NOT_FOUND.value())
                        .errorMessage(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(AccessModificationException.class)
    public ResponseEntity<ErrorMessage> handleAccessModification(AccessModificationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorMessage.builder()
                        .httpCode(HttpStatus.CONFLICT.value())
                        .errorMessage(ex.getMessage())
                        .build());
    }
}

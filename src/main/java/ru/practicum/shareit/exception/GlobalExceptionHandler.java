package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorMessage> handleIllegalState(IllegalStateException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorMessage.builder()
                        .httpCode(HttpStatus.BAD_REQUEST.value())
                        .errorMessage(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorMessage> handleDuplicateEmail(DuplicateEmailException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorMessage.builder()
                        .httpCode(HttpStatus.CONFLICT.value())
                        .errorMessage(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorMessage> handleBusinessLogic(BusinessLogicException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorMessage.builder()
                        .httpCode(HttpStatus.BAD_REQUEST.value())
                        .errorMessage(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(ApproveModificationException.class)
    public ResponseEntity<ErrorMessage> handleApproveModification(ApproveModificationException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorMessage.builder()
                        .httpCode(HttpStatus.FORBIDDEN.value())
                        .errorMessage(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFound(NotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorMessage.builder()
                        .httpCode(HttpStatus.NOT_FOUND.value())
                        .errorMessage(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(AccessModificationException.class)
    public ResponseEntity<ErrorMessage> handleAccessModification(AccessModificationException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorMessage.builder()
                        .httpCode(HttpStatus.CONFLICT.value())
                        .errorMessage(ex.getMessage())
                        .build());
    }
}

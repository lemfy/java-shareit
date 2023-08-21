package ru.practicum.shareit.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorHandlerTest {
    private final ErrorHandler handler = new ErrorHandler();

    @Test
    void handleValidationException() {
        ValidationException ex = new ValidationException("exception");
        ErrorResponse response = handler.handleValidationException(ex);
        assertEquals("exception", response.getError());
    }

    @Test
    void handleMethodArgumentNotValidException() {
        Exception ex = new Exception("exception");
        ErrorResponse response = handler.handleMethodArgumentNotValidException(ex);
        assertEquals("exception", response.getError());
    }

    @Test
    void handleObjectNotFoundException() {
        ItemNotFoundException ex = new ItemNotFoundException(1);
        ErrorResponse response = handler.handleObjectNotFoundException(ex);
        assertEquals("Вещь не найдена: 1 ", response.getError());
    }

    @Test
    void handleThrowable() {
        RuntimeException ex = new RuntimeException("exception");
        ErrorResponse response = handler.handleThrowable(ex);
        assertEquals("Произошла непредвиденная ошибка", response.getError());
    }
}
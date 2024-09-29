package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.service.validators.ValidationException;
import ru.yandex.practicum.filmorate.storage.NotFoundException;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionsHandler {
    static final String EXCEPTION_CAUGHT_MESSAGE = "Caught exception by controller exception handler";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public Map<String, String> validationExceptionsHandler(ValidationException exc) {
        log.warn(EXCEPTION_CAUGHT_MESSAGE, exc);
        return Map.of("error", exc.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public Map<String, String> notFoundExceptionsHandler(NotFoundException exc) {
        log.warn(EXCEPTION_CAUGHT_MESSAGE, exc);
        return Map.of("error", exc.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public Map<String, String> otherExceptionsHandler(Throwable exc) {
        log.warn(EXCEPTION_CAUGHT_MESSAGE, exc);
        return Map.of("error", "Произошла ошибка на сервере.");
    }
}

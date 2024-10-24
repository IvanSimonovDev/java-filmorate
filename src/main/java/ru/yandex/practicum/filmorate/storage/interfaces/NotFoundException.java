package ru.yandex.practicum.filmorate.storage.interfaces;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}

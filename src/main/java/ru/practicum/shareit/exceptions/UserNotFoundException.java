package ru.practicum.shareit.exceptions;

import javax.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(Integer id) {
        super(String.format("Пользователь не найден: %d ", id));
    }
}
package ru.practicum.shareit.exceptions;

import javax.persistence.EntityNotFoundException;

public class ItemNotFoundException extends EntityNotFoundException {
    public ItemNotFoundException(Integer id) {
        super(String.format("Вещь не найдена: %d ", id));
    }
}
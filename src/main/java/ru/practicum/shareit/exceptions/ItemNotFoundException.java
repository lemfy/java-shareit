package ru.practicum.shareit.exceptions;

import javax.persistence.EntityNotFoundException;

public class ItemNotFoundException extends EntityNotFoundException {
    public ItemNotFoundException(Long id) {
        super(String.format("Вещь не найдена: %d ", id));
    }
}
package ru.practicum.shareit.booking.enums;

import ru.practicum.shareit.exceptions.ValidationException;

public enum BookingRequestStatus {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static BookingRequestStatus getStatus(String state) {
        try {
            return BookingRequestStatus.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new ValidationException(String.format("Unknown state: %s", state));
        }
    }
}
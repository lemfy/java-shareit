package ru.practicum.shareit.exceptions;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class WrongBookingParameterException extends RuntimeException {
    private final String parameter;
}
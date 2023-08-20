package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {

    BookingResponseDto addNewBooking(Integer userId, BookingRequestDto bookingRequestDto);

    BookingResponseDto approveBooking(Integer userId, Integer bookingId, Boolean isApproved);

    BookingResponseDto getBookingById(Integer userId, Integer bookingId);

    List<BookingResponseDto> getAllBookings(Integer userId, String state, Integer from, Integer size);

    List<BookingResponseDto> getAllBookingsForOwner(Integer userId, String state, Integer from, Integer size);
}

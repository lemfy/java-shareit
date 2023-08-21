package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Variables;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto addNewBooking(@RequestHeader(Variables.USER_ID) Integer userId,
                                            @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        return bookingService.addNewBooking(userId, bookingRequestDto);
    }

    @PatchMapping(value = "/{bookingId}")
    public BookingResponseDto approveBooking(@RequestHeader(Variables.USER_ID) Integer userId,
                                             @PathVariable Integer bookingId,
                                             @RequestParam(name = "approved") Boolean isApproved) {
        return bookingService.approveBooking(userId, bookingId, isApproved);
    }

    @GetMapping(value = "/{bookingId}")
    public BookingResponseDto getBookingById(@RequestHeader(Variables.USER_ID) Integer userId,
                                             @PathVariable Integer bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> getAllBookings(@RequestHeader(Variables.USER_ID) Integer userId,
                                                   @RequestParam(defaultValue = "ALL", required = false) String state,
                                                   @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                                   @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        return bookingService.getAllBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllBookingsForAllItems(@RequestHeader(Variables.USER_ID) Integer userId,
                                                              @RequestParam(defaultValue = "ALL", required = false) String state,
                                                              @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                                              @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        return bookingService.getAllBookingsForOwner(userId, state, from, size);
    }
}

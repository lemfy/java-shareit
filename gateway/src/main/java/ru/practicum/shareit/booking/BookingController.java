package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.Variables;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> addNewBooking(@RequestHeader(Variables.USER_ID) long userId,
										   @RequestBody @Valid BookingRequestDto requestDto) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.addNewBooking(userId, requestDto);
	}

	@PatchMapping(value = "/{bookingId}")
	public ResponseEntity<Object> approveBooking(@RequestHeader(Variables.USER_ID) Long userId,
												 @PathVariable Long bookingId,
												 @RequestParam(name = "approved") Boolean isApproved) {
		log.info("Approve booking with id={} by userId={}", bookingId, userId);
		return bookingClient.approveBooking(userId, bookingId, isApproved);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBookingById(@RequestHeader(Variables.USER_ID) long userId,
											 @PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBookingById(userId, bookingId);
	}

	@GetMapping
	public ResponseEntity<Object> getAllBookings(@RequestHeader(Variables.USER_ID) long userId,
												 @RequestParam(name = "state", defaultValue = "all") String statePar,
												 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
												 @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingRequestStatus state = BookingRequestStatus.getStatus(statePar);
		log.info("Get booking with state {}, userId={}, from={}, size={}", statePar, userId, from, size);
		return bookingClient.getAllBookings(userId, state, from, size);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getBookingsForOwner(@RequestHeader(Variables.USER_ID) long userId,
											  @RequestParam(name = "state", defaultValue = "all") String statePar,
											  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
											  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingRequestStatus state = BookingRequestStatus.getStatus(statePar);
		log.info("Get booking for all owner items with state {}, userId={}, from={}, size={}", statePar, userId, from, size);
		return bookingClient.getBookingsForOwner(userId, state, from, size);
	}
}
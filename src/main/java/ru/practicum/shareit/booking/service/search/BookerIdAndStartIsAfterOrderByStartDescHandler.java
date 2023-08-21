package ru.practicum.shareit.booking.service.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.enums.BookingRequestStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.booking.enums.BookingRequestStatus.FUTURE;

@Slf4j
public class BookerIdAndStartIsAfterOrderByStartDescHandler extends BookingSearcher {
    public BookerIdAndStartIsAfterOrderByStartDescHandler(BookingRepository bookingRepository) {
        super(bookingRepository);
    }

    @Override
    public Boolean isCorrectState(BookingRequestStatus status) {
        return status == FUTURE;
    }

    @Override
    public List<Booking> findBookings(long userId, Pageable pageable) {
        log.info("{} {}", getClass().getSimpleName(), userId);
        return bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now(), pageable);
    }
}

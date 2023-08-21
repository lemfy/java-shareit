package ru.practicum.shareit.booking.service.search;

import static ru.practicum.shareit.booking.enums.BookingRequestStatus.PAST;

import java.time.LocalDateTime;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.enums.BookingRequestStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;

@Slf4j
public class BookerIdAndEndIsBeforeOrderByStartDescHandler extends BookingSearcher {

    public BookerIdAndEndIsBeforeOrderByStartDescHandler(BookingRepository bookingRepository) {
        super(bookingRepository);
    }

    @Override
    public Boolean isCorrectState(BookingRequestStatus status) {
        return status == PAST;
    }

    @Override
    public List<Booking> findBookings(long userId, Pageable pageable) {
        log.info("{} {}", getClass().getSimpleName(), userId);
        return bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(userId,
                LocalDateTime.now(), pageable);
    }
}

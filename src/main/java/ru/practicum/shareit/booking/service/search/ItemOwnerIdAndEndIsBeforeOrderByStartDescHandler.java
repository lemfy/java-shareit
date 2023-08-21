package ru.practicum.shareit.booking.service.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.enums.BookingRequestStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.booking.enums.BookingRequestStatus.PAST;

@Slf4j
public class ItemOwnerIdAndEndIsBeforeOrderByStartDescHandler extends BookingSearcher {
    public ItemOwnerIdAndEndIsBeforeOrderByStartDescHandler(BookingRepository bookingRepository) {
        super(bookingRepository);
    }

    @Override
    public Boolean isCorrectState(BookingRequestStatus status) {
        return status == PAST;
    }

    @Override
    public List<Booking> findBookings(Integer userId, Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        log.info("{} {} {}", getClass().getSimpleName(), userId, now);
        return bookingRepository.findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(userId, now, pageable);
    }
}
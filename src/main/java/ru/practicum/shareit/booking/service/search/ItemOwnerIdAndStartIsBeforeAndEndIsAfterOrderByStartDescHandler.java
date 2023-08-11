package ru.practicum.shareit.booking.service.search;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.enums.BookingRequestStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class ItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDescHandler extends BookingSearcher {
    public ItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDescHandler(BookingRepository bookingRepository) {
        super(bookingRepository);
    }

    @Override
    public Boolean isCorrectState(BookingRequestStatus status) {
        return status == BookingRequestStatus.CURRENT;
    }

    @Override
    public List<Booking> findBookings(Integer userId) {
        LocalDateTime now = LocalDateTime.now();
        log.info("{} {} {}", getClass().getSimpleName(), userId, now);
        return bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId, now, now);
    }
}
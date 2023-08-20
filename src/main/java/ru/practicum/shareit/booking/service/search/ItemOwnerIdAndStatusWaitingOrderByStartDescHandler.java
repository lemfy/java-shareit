package ru.practicum.shareit.booking.service.search;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.enums.BookingRequestStatus;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.util.List;

@Slf4j
public class ItemOwnerIdAndStatusWaitingOrderByStartDescHandler extends BookingSearcher {
    public ItemOwnerIdAndStatusWaitingOrderByStartDescHandler(BookingRepository bookingRepository) {
        super(bookingRepository);
    }

    @Override
    public Boolean isCorrectState(BookingRequestStatus status) {
        return status == BookingRequestStatus.WAITING;
    }

    @Override
    public List<Booking> findBookings(Integer userId) {
        log.info("{} {}", getClass().getSimpleName(), userId);
        return bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
    }
}

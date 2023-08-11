package ru.practicum.shareit.booking.service.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.enums.BookingRequestStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public abstract class BookingSearcher {
    BookingSearcher bookingSearcher;
    protected final BookingRepository bookingRepository;

    abstract Boolean isCorrectState(BookingRequestStatus status);

    abstract List<Booking> findBookings(Integer userId);

    public List<Booking> find(BookingRequestStatus status, Integer userId) {
        log.info("BookingSearcher.find({},{})", status, userId);
        if (isCorrectState(status)) {
            log.info("status {} correct by {}", status, getClass().getSimpleName());
            return findBookings(userId);
        }
        if (bookingSearcher == null) {
            return new ArrayList<>();
        }
        return bookingSearcher.find(status, userId);
    }

    public static BookingSearcher link(BookingSearcher item1, BookingSearcher... item2) {
        BookingSearcher head = item1;
        for (BookingSearcher item : item2) {
            head.bookingSearcher = item;
            head = item;
        }
        return item1;
    }
}

package ru.practicum.shareit.booking.service.search;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.enums.BookingRequestStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;

@RequiredArgsConstructor
@Slf4j
public abstract class BookingSearcher {

    public BookingSearcher next;

    protected final BookingRepository bookingRepository;

    public abstract Boolean isCorrectState(BookingRequestStatus status);

    public abstract List<Booking> findBookings(long userId, Pageable pageable);

    public List<Booking> find(BookingRequestStatus status, long userId, Pageable pageable) {
        log.info("BookingSearcher.find({},{})", status, userId);
        if (isCorrectState(status)) {
            log.info("status {} correct by {}", status, getClass().getSimpleName());
            return findBookings(userId, pageable);
        }
        if (next == null) {
            return new ArrayList<>();
        }
        return next.find(status, userId, pageable);
    }

    public static BookingSearcher link(BookingSearcher item1, BookingSearcher... item2) {
        BookingSearcher head = item1;
        for (BookingSearcher item : item2) {
            head.next = item;
            head = item;
        }
        return item1;
    }
}

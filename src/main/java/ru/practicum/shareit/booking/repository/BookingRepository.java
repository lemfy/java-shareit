package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.BookingShortForItem;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByBookerIdOrderByStartDesc(Integer bookerId, Pageable pageable);

    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Integer bookerId, LocalDateTime end, Pageable pageable);

    List<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(Integer bookerId, LocalDateTime start, Pageable pageable);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Integer bookerId, BookingStatus status, Pageable pageable);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByIdAsc(Integer bookerId, LocalDateTime start,
                                                                          LocalDateTime end, Pageable pageable);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Integer owner, Pageable pageable);

    List<Booking> findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Integer owner, LocalDateTime end, Pageable pageable);

    List<Booking> findByItemOwnerIdAndStartIsAfterOrderByStartDesc(Integer owner, LocalDateTime start, Pageable pageable);

    List<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Integer owner, LocalDateTime start,
                                                                                 LocalDateTime end, Pageable pageable);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Integer owner, BookingStatus status, Pageable pageable);

    List<BookingShortForItem> findByItemInAndStatus(List<Item> items, BookingStatus status);

    List<BookingShortForItem> findByItemAndStatus(Item item, BookingStatus status);

    BookingShortForItem findFirstByItemAndBookerAndStatus(Item item, User booker, BookingStatus status);
}
package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.BookingShortForItem;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Integer bookerId, LocalDateTime end);

    List<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(Integer bookerId, LocalDateTime start);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Integer bookerId, BookingStatus status);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartAsc(Integer bookerId, LocalDateTime start,
                                                                             LocalDateTime end);

    List<Booking> findByBookerIdOrderByStartDesc(Integer bookerId);

    List<Booking> findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Integer owner, LocalDateTime end);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Integer owner, BookingStatus status);

    List<Booking> findByItemOwnerIdAndStartIsAfterOrderByStartDesc(Integer owner, LocalDateTime start);

    List<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Integer owner, LocalDateTime start, LocalDateTime end);

    BookingShortForItem findFirstByItemAndBookerAndStatus(Item item, User booker, BookingStatus status);

    List<BookingShortForItem> findByItemInAndStatus(List<Item> items, BookingStatus status);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Integer owner);

    List<BookingShortForItem> findByItemAndStatus(Item item, BookingStatus status);
}

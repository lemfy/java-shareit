package ru.practicum.shareit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    private User user1;
    private User user2;
    private Item item;
    private Booking booking;

    private Sort sort = Sort.by("start");

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(new User(-1L, "name1", "mail1@yandex.com"));
        user2 = userRepository.save(new User(-1L, "name2", "mail2@yandex.com"));
        item = itemRepository.save(new Item(-1L, "itemName", "description", true, user1, null));
        LocalDateTime from = LocalDateTime.now().plusMinutes(1);
        LocalDateTime till = from.plusDays(1);
        booking = bookingRepository.save(new Booking(-1L, from, till, item, user2, BookingStatus.APPROVED));
    }

    @Test
    void findByBooker_Id() {
        List<Booking> res = bookingRepository.findByBookerIdOrderByStartDesc(user2.getId(), Pageable.unpaged());
        assertNotNull(res);
        assertTrue(!res.isEmpty());
        assertEquals(booking, res.get(0));
    }

    @Test
    void findByItemOwnerIdOrderByStartDesc() {
        List<Booking> res = bookingRepository.findByItemOwnerIdOrderByStartDesc(user1.getId(), Pageable.unpaged());
        assertNotNull(res);
        assertTrue(!res.isEmpty());
        assertEquals(booking, res.get(0));
    }

    @Test
    void findByBookerIdAndEndIsBeforeOrderByStartDesc() {
        LocalDateTime date = LocalDateTime.now().plusDays(2);
        List<Booking> res = bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(user2.getId(), date, Pageable.unpaged());
        assertNotNull(res);
        assertTrue(!res.isEmpty());
        assertEquals(booking, res.get(0));
    }

    @Test
    void findByBookerIdAndStartIsAfterOrderByStartDesc() {
        LocalDateTime date = LocalDateTime.now().minusHours(1);
        List<Booking> res = bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(user2.getId(), date, Pageable.unpaged());
        assertNotNull(res);
        assertTrue(!res.isEmpty());
        assertEquals(booking, res.get(0));
    }

    @Test
    void findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByIdAsc() {
        LocalDateTime date = LocalDateTime.now().plusHours(1);
        List<Booking> res = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByIdAsc(user2.getId(), date, date, Pageable.unpaged());
        assertNotNull(res);
        assertTrue(!res.isEmpty());
        assertEquals(booking, res.get(0));
    }

    @Test
    void findByBookerIdAndStatusOrderByStartDesc() {
        List<Booking> res = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(user2.getId(), BookingStatus.APPROVED, Pageable.unpaged());
        assertNotNull(res);
        assertTrue(!res.isEmpty());
        assertEquals(booking, res.get(0));
    }

    @Test
    void findByItemOwnerIdAndEndIsBeforeOrderByStartDesc() {
        LocalDateTime date = LocalDateTime.now().plusDays(2);
        List<Booking> res = bookingRepository.findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(user1.getId(), date, Pageable.unpaged());
        assertNotNull(res);
        assertTrue(!res.isEmpty());
        assertEquals(booking, res.get(0));
    }

    @Test
    void findByItemOwnerIdAndStartIsAfterOrderByStartDesc() {
        LocalDateTime date = LocalDateTime.now().minusDays(2);
        List<Booking> res = bookingRepository.findByItemOwnerIdAndStartIsAfterOrderByStartDesc(user1.getId(), date, Pageable.unpaged());
        assertNotNull(res);
        assertTrue(!res.isEmpty());
        assertEquals(booking, res.get(0));
    }

    @Test
    void findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc() {
        LocalDateTime date = LocalDateTime.now().plusHours(2);
        List<Booking> res = bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(user1.getId(), date, date, Pageable.unpaged());
        assertNotNull(res);
        assertTrue(!res.isEmpty());
        assertEquals(booking, res.get(0));
    }

    @Test
    void findByItemOwnerIdAndStatusOrderByStartDesc() {
        List<Booking> res = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(user1.getId(), BookingStatus.APPROVED, Pageable.unpaged());
        assertNotNull(res);
        assertTrue(!res.isEmpty());
        assertEquals(booking, res.get(0));
    }

}

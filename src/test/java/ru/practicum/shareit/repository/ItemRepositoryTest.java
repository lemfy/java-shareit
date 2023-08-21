package ru.practicum.shareit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository requestRepository;

    private User user1;
    private User user2;
    private Item item;
    private Comment comment;
    private ItemRequest request;

    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        user1 = userRepository.save(new User(-1L, "name1", "mail1@yandex.com"));
        user2 = userRepository.save(new User(-1L, "name2", "mail2@yandex.com"));
        request = requestRepository.save(new ItemRequest(-1L, "description", user2, now));
        item = itemRepository.save(new Item(-1L, "itemName", "description", true, user1, request));
        comment = commentRepository.save(new Comment(-1L, "comment", item, user2, now));
    }

    @Test
    void search() {
        List<Item> res = itemRepository.search("tem", Pageable.unpaged());
        assertNotNull(res);
        assertTrue(!res.isEmpty());
        assertEquals(item, res.get(0));
    }

    @Test
    void findByOwnerOrderByIdAsc() {
        List<Item> res = itemRepository.findByOwnerOrderByIdAsc(user1, Pageable.unpaged());
        assertNotNull(res);
        assertTrue(!res.isEmpty());
        assertEquals(item, res.get(0));
    }

    @Test
    void findByRequestId() {
        List<Item> res = itemRepository.findByRequestId(request.getId());
        assertNotNull(res);
        assertTrue(res.size() > 0);
        assertEquals(item, res.get(0));
    }

    @Test
    void findByRequestIdIn() {
        List<Item> res = itemRepository.findByRequestIdIn(List.of(request.getId()));
        assertNotNull(res);
        assertTrue(res.size() > 0);
        assertEquals(item, res.get(0));
    }

}

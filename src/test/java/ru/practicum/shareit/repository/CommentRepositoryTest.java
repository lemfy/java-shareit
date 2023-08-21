package ru.practicum.shareit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CommentRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    private Item item;
    private Comment comment;

    @BeforeEach
    void setUp() {
        User user1 = userRepository.save(new User(-1, "name1", "mail1@yandex.com"));
        User user2 = userRepository.save(new User(-1, "name2", "mail2@yandex.com"));
        item = itemRepository.save(new Item(-1, "itemName", "description", true, user1, null));
        LocalDateTime now = LocalDateTime.now();
        comment = commentRepository.save(new Comment(-1, "comment", item, user2, now));
    }

    @Test
    void findByItem() {
        List<Comment> res = commentRepository.findByItem(item);
        assertNotNull(res);
        assertFalse(res.isEmpty());
        assertEquals(comment, res.get(0));
    }
}
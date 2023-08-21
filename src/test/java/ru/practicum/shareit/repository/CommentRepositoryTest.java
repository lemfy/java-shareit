package ru.practicum.shareit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
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
public class CommentRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private Item item;
    private Comment comment;

    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(new User(-1L, "name1", "mail1@yandex.com"));
        user2 = userRepository.save(new User(-1L, "name2", "mail2@yandex.com"));
        item = itemRepository.save(new Item(-1L, "itemName", "description", true, user1, null));
        now = LocalDateTime.now();
        comment = commentRepository.save(new Comment(-1L, "comment", item, user2, now));
    }

    @Test
    void findByItem() {
        List<Comment> res = commentRepository.findByItem(item);
        assertNotNull(res);
        assertTrue(res.size() > 0);
        assertEquals(comment, res.get(0));
    }

}

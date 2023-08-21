package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.BookingShortForItem;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.RequestNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplTest {
    @MockBean
    private BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;
    private ItemDto itemDto;
    private ItemDto itemDto2;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        itemDto = ItemDto.builder()
                .name("item1 name")
                .description("item1 description")
                .available(true)
                .build();

        itemDto2 = ItemDto.builder()
                .name("item2 name")
                .description("item2 description")
                .available(true)
                .build();

        userDto = UserDto.builder()
                .name("user1")
                .email("user1@user.com")
                .build();
    }

    @Test
    void addNewItemReturnItemTest() {
        Integer userId = userService.createUser(userDto).getId();
        ItemDto actualItemDto = itemService.createItem(userId, itemDto);

        Assertions.assertEquals(itemDto.getName(), actualItemDto.getName(), "Name не совпадают");
        Assertions.assertEquals(itemDto.getDescription(), actualItemDto.getDescription(), "Description не совпадают");
        Assertions.assertEquals(itemDto.getAvailable(), actualItemDto.getAvailable(), "Available не совпадают");
    }

    @Test
    void addNewItemWithWrongRequestIdReturnRequestNotFoundExceptionTest() {
        Integer userId = userService.createUser(userDto).getId();
        itemDto.setRequestId(1);
        assertThrows(RequestNotFoundException.class,
                () -> itemService.createItem(userId, itemDto));
    }

    @Test
    void addNewItemWithWrongUserIdReturnUserNotFoundExceptionTest() {
        assertThrows(UserNotFoundException.class,
                () -> itemService.createItem(999, itemDto));
    }

    @Test
    void getAllItemsTest() {
        Integer userId = userService.createUser(userDto).getId();
        itemService.createItem(userId, itemDto);
        itemService.createItem(userId, itemDto2);
        Assertions.assertEquals(2, itemService.getAllItems(userId, 0, 10).size(),
                "Количество вещей не совпадает");
    }

    @Test
    void getItemByIdReturnItemTest() {
        Integer userId = userService.createUser(userDto).getId();
        Integer itemId = itemService.createItem(userId, itemDto).getId();
        ItemDto actualItemDto = itemService.getItem(userId, itemId);

        Assertions.assertEquals(itemDto.getName(), actualItemDto.getName(), "Name не совпадают");
        Assertions.assertEquals(itemDto.getDescription(), actualItemDto.getDescription(), "Description не совпадают");
        Assertions.assertEquals(itemDto.getAvailable(), actualItemDto.getAvailable(), "Available не совпадают");
    }

    @Test
    void getItemByIdReturnItemNotFoundExceptionTest() {
        Integer userId = userService.createUser(userDto).getId();
        itemService.createItem(userId, itemDto);

        assertThrows(ItemNotFoundException.class, () -> itemService.getItem(userId, 999));
    }

    @Test
    void deleteItemTest() {
        Integer userId = userService.createUser(userDto).getId();
        Integer itemId = itemService.createItem(userId, itemDto).getId();

        Assertions.assertEquals(1, itemService.getAllItems(userId, 0, 10).size(),
                "Количество вещей не совпадает");
        itemService.deleteItem(userId, itemId);
        Assertions.assertEquals(0, itemService.getAllItems(userId, 0, 10).size(),
                "Количество вещей не совпадает");
    }

    @Test
    void deleteItemWithWrongItemIdReturnItemNotFoundExceptionTest() {
        Integer userId = userService.createUser(userDto).getId();
        assertThrows(ItemNotFoundException.class, () -> itemService.deleteItem(userId, 999));
    }

    @Test
    void deleteItemWithWrongOwnerIdReturnEntityNotFoundExceptionTest() {
        Integer userId = userService.createUser(userDto).getId();
        Integer itemId = itemService.createItem(userId, itemDto).getId();
        assertThrows(EntityNotFoundException.class,
                () -> itemService.deleteItem(999, itemId));
    }

    @Test
    void updateItemReturnUpdatedItemTest() {
        Integer userId = userService.createUser(userDto).getId();
        Integer itemId = itemService.createItem(userId, itemDto).getId();

        ItemDto updatedItemDto = ItemDto.builder()
                .id(itemId)
                .name("item updated name")
                .description("item updated description")
                .available(true)
                .build();

        Assertions.assertEquals(updatedItemDto, itemService.updateItem(userId, itemId, updatedItemDto),
                "Данные вещи не совпадают");
    }

    @Test
    void updateItemDescriptionReturnUpdatedItemTest() {
        Integer userId = userService.createUser(userDto).getId();
        Integer itemId = itemService.createItem(userId, itemDto).getId();

        ItemDto updatedItemDto = ItemDto.builder()
                .description("item updated description")
                .build();

        Assertions.assertEquals(updatedItemDto.getDescription(), itemService.updateItem(userId, itemId, updatedItemDto).getDescription(),
                "Данные вещи не совпадают");
    }

    @Test
    void updateItemNameReturnUpdatedItemTest() {
        Integer userId = userService.createUser(userDto).getId();
        Integer itemId = itemService.createItem(userId, itemDto).getId();
        ItemDto updatedItemDto = ItemDto.builder()
                .name("updated name")
                .build();

        Assertions.assertEquals(updatedItemDto.getName(), itemService.updateItem(userId, itemId, updatedItemDto).getName(),
                "Данные вещи не совпадают");
    }

    @Test
    void updateItemWithWrongItemIdReturnItemNotFoundExceptionTest() {
        Integer userId = userService.createUser(userDto).getId();
        ItemDto updatedItemDto = ItemDto.builder()
                .name("updated name")
                .build();
        assertThrows(ItemNotFoundException.class,
                () -> itemService.updateItem(userId, 999, updatedItemDto));
    }

    @Test
    void updateItemWithWrongOwnerIdReturnEntityNotFoundExceptionTest() {
        Integer userId = userService.createUser(userDto).getId();
        Integer itemId = itemService.createItem(userId, itemDto).getId();
        ItemDto updatedItemDto = ItemDto.builder()
                .name("updated name")
                .build();
        assertThrows(EntityNotFoundException.class,
                () -> itemService.updateItem(999, itemId, updatedItemDto));
    }

    @Test
    void searchItemsReturnItemsTest() {
        Integer userId = userService.createUser(userDto).getId();
        Integer itemId = itemService.createItem(userId, itemDto).getId();
        itemDto.setId(itemId);
        itemService.createItem(userId, itemDto2);
        List<ItemDto> expectedItems = List.of(itemDto);

        Assertions.assertEquals(expectedItems, itemService.searchItems("item1", 0, 10),
                "Данные поиска не совпадают");
    }

    @Test
    void searchItemsReturnEmptyListTest() {
        Integer userId = userService.createUser(userDto).getId();
        itemService.createItem(userId, itemDto);
        itemService.createItem(userId, itemDto2);

        Assertions.assertEquals(Collections.emptyList(), itemService.searchItems("", 0, 10),
                "Данные поиска не совпадают");
    }

    @Test
    void addCommentReturnValidationExceptionTest() {
        Integer userId = userService.createUser(userDto).getId();
        Integer itemId = itemService.createItem(userId, itemDto).getId();
        CommentRequestDto comment = CommentRequestDto.builder()
                .text("Test comment")
                .build();
        assertThrows(ValidationException.class, () -> itemService.addComment(userId, itemId, comment));
    }

    @Test
    void addCommentWithWrongUserReturnUserNotFoundExceptionTest() {
        Integer userId = userService.createUser(userDto).getId();
        Integer itemId = itemService.createItem(userId, itemDto).getId();
        CommentRequestDto comment = CommentRequestDto.builder()
                .text("Test comment")
                .build();
        assertThrows(UserNotFoundException.class, () -> itemService.addComment(999, itemId, comment));
    }

    @Test
    void addCommentReturnCommentTest() {
        Integer userId = userService.createUser(userDto).getId();
        Integer itemId = itemService.createItem(userId, itemDto).getId();
        itemService.createItem(userId, itemDto);
        CommentRequestDto comment = CommentRequestDto.builder()
                .text("Test comment")
                .build();
        LocalDateTime start = LocalDateTime.now().minusDays(20);
        LocalDateTime end = LocalDateTime.now().minusDays(2);
        BookingShortForItem bookingShortForItem = BookingShortForItem.builder()
                .bookerId(userId)
                .itemId(itemId)
                .start(start)
                .end(end)
                .build();
        when(bookingRepository.findFirstByItemAndBookerAndStatus(any(), any(), any()))
                .thenReturn(bookingShortForItem);

        Assertions.assertEquals(userId, bookingShortForItem.getBookerId());
        Assertions.assertEquals(itemId, bookingShortForItem.getItemId());
        Assertions.assertEquals(start, bookingShortForItem.getStart());
        Assertions.assertEquals(end, bookingShortForItem.getEnd());
        Assertions.assertEquals(comment.getText(), itemService.addComment(userId, itemId, comment).getText(),
                "Комментарии не совпадают");
    }
}
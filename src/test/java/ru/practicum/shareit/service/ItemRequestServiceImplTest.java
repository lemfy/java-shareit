package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImplTest {

    private final ItemRequestService itemRequestService;
    private final UserService userService;

    private ItemRequestDto itemRequestDto;
    private ItemRequestResponseDto itemRequestResponseDto;
    private UserDto userDto;
    private Integer userId;

    @BeforeEach
    void setUp() {
        itemRequestDto = ItemRequestDto.builder()
                .description("item request description")
                .build();

        itemRequestResponseDto = ItemRequestResponseDto.builder()
                .description("item request description")
                .build();

        userDto = UserDto.builder()
                .name("user")
                .email("user@user.com")
                .build();

        userId = userService.createUser(userDto).getId();
    }

    @Test
    void addItemRequestReturnItemRequestTest() {
        ItemRequestResponseDto actual = itemRequestService.createRequest(userId, itemRequestDto);
        Assertions.assertEquals(itemRequestDto.getDescription(), actual.getDescription(), "Description не совпадают");
    }

    @Test
    void addItemRequestByWrongUserReturnUserNotFoundExceptionTest() {
        assertThrows(UserNotFoundException.class, () -> itemRequestService.createRequest(999, itemRequestDto));
    }

    @Test
    void getAllOwnItemRequestsReturnItemRequestsTest() {
        itemRequestService.createRequest(userId, itemRequestDto);
        Assertions.assertEquals(1, itemRequestService.getAllOwnItemRequests(userId).size(), "Количество запросов не совпадает");
    }

    @Test
    void getAllOthersItemRequestsReturnItemRequestsTest() {
        Integer otherUserId = userService.createUser(UserDto.builder().name("otherUser").email("otheruser@user.com").build()).getId();
        itemRequestService.createRequest(otherUserId, itemRequestDto);
        Assertions.assertEquals(1, itemRequestService.getAllOthersItemRequests(userId, 0, 5).size(), "Количество запросов не совпадает");
    }

    @Test
    void getRequestByIdReturnItemRequestTest() {
        Integer requestId = itemRequestService.createRequest(userId, itemRequestDto).getId();
        Assertions.assertEquals(itemRequestResponseDto.getDescription(),
                itemRequestService.getItemRequest(userId, requestId).getDescription(), "Запросы не совпадают");
    }
}
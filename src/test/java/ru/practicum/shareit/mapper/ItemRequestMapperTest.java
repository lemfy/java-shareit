package ru.practicum.shareit.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.practicum.shareit.request.mapper.ItemRequestMapper.toItemRequestResponseDto;

class ItemRequestMapperTest {

    @Test
    void toItemRequestResponseDtoTest() {
        User user = User.builder().name("Test name").build();
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Test item description")
                .requestor(user)
                .created(LocalDateTime.of(2022, 10, 22, 10,0, 5))
                .build();
        ItemDto itemDto = ItemDto.builder()
                .name("Test item name")
                .description("Test item description")
                .build();
        ItemRequestResponseDto itemRequestResponseDto = toItemRequestResponseDto(itemRequest,
                List.of(itemDto));

        assertEquals(itemRequest.getId(), itemRequestResponseDto.getId());
        assertEquals(itemRequest.getDescription(), itemRequestResponseDto.getDescription());
        assertEquals(user, itemRequest.getRequestor());
        assertNotNull(itemRequest.toString());
        assertEquals(itemRequest.getCreated(), itemRequestResponseDto.getCreated());
        assertEquals(itemDto.getName(), itemRequestResponseDto.getItems().get(0).getName());
    }

    @Test
    void toItemRequestResponseListDtoTest() {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Test item description")
                .requestor(User.builder().name("Test name").build())
                .created(LocalDateTime.of(2022, 10, 22, 10,0, 5))
                .build();
        ItemDto itemDto = ItemDto.builder()
                .name("Test item name")
                .description("Test item description")
                .requestId(1L)
                .build();
        List<ItemRequestResponseDto> itemRequestResponseDto = toItemRequestResponseDto(List.of(itemRequest),
                List.of(itemDto));

        assertEquals(itemRequest.getId(), itemRequestResponseDto.get(0).getId());
        assertEquals(itemRequest.getDescription(), itemRequestResponseDto.get(0).getDescription());
        assertEquals(itemRequest.getCreated(), itemRequestResponseDto.get(0).getCreated());
        assertEquals(itemDto.getName(), itemRequestResponseDto.get(0).getItems().get(0).getName());
    }

    @Test
    void itemRequestEqualsTest() {
        ItemRequest itemRequest1 = ItemRequest.builder()
                .id(1L)
                .description("Test item description")
                .requestor(User.builder().name("Test name").build())
                .created(LocalDateTime.of(2022, 10, 22, 10,0, 5))
                .build();
        ItemRequest itemRequest2 = ItemRequest.builder()
                .id(1L)
                .description("Test item description")
                .requestor(User.builder().name("Test name").build())
                .created(LocalDateTime.of(2022, 10, 22, 10,0, 5))
                .build();
        assertEquals(itemRequest1,itemRequest2);
    }

}

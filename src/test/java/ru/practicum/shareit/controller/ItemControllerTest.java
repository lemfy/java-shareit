package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.Variables;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class ItemControllerTest {
    @MockBean
    private ItemService itemService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        itemDto = ItemDto.builder()
                .id(1)
                .name("item1 name")
                .description("item1 description")
                .available(true)
                .build();
    }

    @Test
    void getAllItems() throws Exception {
        when(itemService.getAllItems(anyInt(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items")
                        .header(Variables.USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())));
    }

    @Test
    void getItemById() throws Exception {
        when(itemService.getItem(anyInt(), anyInt()))
                .thenReturn(itemDto);

        mvc.perform(get("/items/{id}", 1)
                        .header(Variables.USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    void addNewItem() throws Exception {
        when(itemService.createItem(anyInt(), any()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Variables.USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    void updateItem() throws Exception {
        ItemDto itemUpdatedDto = ItemDto.builder()
                .id(1)
                .name("item1 updated name")
                .description("item1 updated description")
                .available(true)
                .build();
        when(itemService.updateItem(anyInt(), anyInt(), any()))
                .thenReturn(itemUpdatedDto);

        mvc.perform(patch("/items/{itemId}", 1)
                        .content(mapper.writeValueAsString(itemUpdatedDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Variables.USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemUpdatedDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(itemUpdatedDto.getName())))
                .andExpect(jsonPath("$.description", is(itemUpdatedDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemUpdatedDto.getAvailable())));
    }

    @Test
    void deleteItem() throws Exception {
        mvc.perform(delete("/items/{itemId}", itemDto.getId())
                        .header(Variables.USER_ID, "1"))
                .andExpect(status().isOk());
    }

    @Test
    void searchItems() throws Exception {
        String text = "description";

        when(itemService.searchItems(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                        .param("text", text)
                        .param("from", "0")
                        .param("size", "5")
                        .header(Variables.USER_ID, "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())));
    }

    @Test
    void addComment() throws Exception {
        CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                .id(1)
                .authorName("Test author")
                .text("Test text")
                .created(LocalDateTime.of(2022, 10, 22, 10, 20, 5))
                .build();

        when(itemService.addComment(anyInt(), anyInt(), any()))
                .thenReturn(commentResponseDto);

        mvc.perform(post("/items/{itemId}/comment", 1)
                        .content(mapper.writeValueAsString(commentResponseDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Variables.USER_ID, "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentResponseDto.getId()), Integer.class))
                .andExpect(jsonPath("$.authorName", is(commentResponseDto.getAuthorName())))
                .andExpect(jsonPath("$.text", is(commentResponseDto.getText())))
                .andExpect(jsonPath("$.created", is(commentResponseDto.getCreated().toString())));
    }
}
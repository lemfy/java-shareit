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
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class ItemRequestControllerTest {

    @MockBean
    private ItemRequestService requestService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private ItemRequestResponseDto itemRequestResponseDto;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        itemRequestResponseDto = ItemRequestResponseDto.builder()
                .id(1L)
                .description("Test description")
                .build();

        itemRequestDto = ItemRequestDto.builder()
                .description("Test description")
                .build();
    }

    @Test
    void addItemRequestReturnItemRequestTest() throws Exception {
        when(requestService.createRequest(anyLong(), any()))
                .thenReturn(itemRequestResponseDto);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Variables.USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemRequestResponseDto.getDescription())));
    }

    @Test
    void getAllOwnItemRequestsReturnItemRequestsTest() throws Exception {
        when(requestService.getAllOwnItemRequests(anyLong()))
                .thenReturn(List.of(itemRequestResponseDto));

        mvc.perform(get("/requests")
                        .header(Variables.USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description", is(itemRequestResponseDto.getDescription())));
    }

    @Test
    void getAllOthersItemRequestsReturnItemRequestsTest() throws Exception {
        when(requestService.getAllOthersItemRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemRequestResponseDto));

        mvc.perform(get("/requests/all")
                        .param("from", "0")
                        .param("size", "5")
                        .header(Variables.USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description", is(itemRequestResponseDto.getDescription())));
    }

    @Test
    void getItemRequestByIdReturnItemRequestTest() throws Exception {
        when(requestService.getItemRequest(anyLong(), anyLong()))
                .thenReturn(itemRequestResponseDto);

        mvc.perform(get("/requests/{requestId}", 1L)
                        .param("from", "0")
                        .param("size", "5")
                        .header(Variables.USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemRequestResponseDto.getDescription())));
    }
}
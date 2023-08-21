package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestResponseDto createRequest(Integer userId, ItemRequestDto itemRequestDto);

    ItemRequestResponseDto getItemRequest(Integer userId, Integer requestId);

    List<ItemRequestResponseDto> getAllOwnItemRequests(Integer userId);

    List<ItemRequestResponseDto> getAllOthersItemRequests(Integer userId, int from, int size);
}
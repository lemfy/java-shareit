package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestResponseDto createRequest(Long userId, ItemRequestDto itemRequestDto);

    ItemRequestResponseDto getItemRequest(Long userId, Long requestId);

    List<ItemRequestResponseDto> getAllOwnItemRequests(Long userId);

    List<ItemRequestResponseDto> getAllOthersItemRequests(Long userId, int from, int size);
}
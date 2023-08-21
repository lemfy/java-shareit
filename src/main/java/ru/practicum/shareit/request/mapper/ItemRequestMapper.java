package ru.practicum.shareit.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemRequestMapper {

    public ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest, List<ItemDto> itemDto) {
        return ItemRequestResponseDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(itemDto)
                .build();
    }

    public List<ItemRequestResponseDto> toItemRequestResponseDto(List<ItemRequest> itemRequests, List<ItemDto> itemDto) {
        List<ItemRequestResponseDto> dto = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            dto.add(toItemRequestResponseDto(itemRequest,
                    itemDto.stream()
                            .filter(item -> item.getRequestId().equals(itemRequest.getId()))
                            .collect(Collectors.toList())
            ));
        }
        return dto;
    }
}
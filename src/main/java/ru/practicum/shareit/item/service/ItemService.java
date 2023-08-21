package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto getItem(Long userId, Long itemId);

    List<ItemDto> getAllItems(Long userId, int  from, int  size);

    ItemDto updateItem(Long id, Long itemId, ItemDto itemDto);

    void deleteItem(Long userId, Long itemId);

    List<ItemDto> searchItems(String text, int  from, int  size);

    CommentResponseDto addComment(Long userId, Long itemId, CommentRequestDto text);
}
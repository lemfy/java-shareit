package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Integer userId, ItemDto itemDto);

    ItemDto getItem(Integer userId, Integer itemId);

    List<ItemDto> getAllItems(Integer userId, Integer from, Integer size);

    ItemDto updateItem(Integer id, Integer itemId, ItemDto itemDto);

    void deleteItem(Integer userId, Integer itemId);

    List<ItemDto> searchItems(String text, Integer from, Integer size);

    CommentResponseDto addComment(Integer userId, Integer itemId, CommentRequestDto text);
}
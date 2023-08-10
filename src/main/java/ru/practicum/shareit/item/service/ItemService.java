package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Integer ownerId, ItemDto itemDto);

    ItemDto getItem(Integer ownerId, Integer itemId);

    List<ItemDto> getAllItems(Integer ownerId);

    ItemDto updateItem(Integer ownerId, Integer itemId, ItemDto itemDto);

    List<ItemDto> searchItems(String text);

    CommentDto addComment(Integer authorId, Integer itemId, CommentDto commentDto);
}
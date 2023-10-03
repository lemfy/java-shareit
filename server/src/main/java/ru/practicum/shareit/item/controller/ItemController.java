package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Variables;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader(Variables.USER_ID) Long userId,
                              @RequestBody @Validated ItemDtoCreate itemDto) {
        return itemService.createItem(userId, itemDto);
    }

    @GetMapping(value = "/{itemId}")
    public ItemDto getItem(@RequestHeader(Variables.USER_ID) Long userId,
                           @PathVariable Long itemId) {
        return itemService.getItem(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader(Variables.USER_ID) Long userId,
                                     @RequestParam(defaultValue = "0") int  from,
                                     @RequestParam(defaultValue = "10") int  size) {
        return itemService.getAllItems(userId, from, size);
    }

    @PatchMapping(value = "/{itemId}")
    public ItemDto updateItem(@RequestHeader(Variables.USER_ID) Long userId,
                              @PathVariable Long itemId,
                              @RequestBody ItemDtoUpdate itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(Variables.USER_ID) Long userId,
                           @PathVariable Long itemId) {
        itemService.deleteItem(userId, itemId);
    }

    @GetMapping("search")
    public List<ItemDto> searchItems(@RequestParam String text,
                                     @RequestParam(defaultValue = "0") int  from,
                                     @RequestParam(defaultValue = "10") int  size) {
        return itemService.searchItems(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(@RequestHeader(Variables.USER_ID) Long userId,
                                         @PathVariable Long itemId,
                                         @RequestBody @Validated CommentRequestDto text) {
        return itemService.addComment(userId, itemId, text);
    }
}


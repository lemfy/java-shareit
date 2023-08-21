package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Variables;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader(Variables.USER_ID) Integer userId,
                              @RequestBody @Validated ItemDto itemDto) {
        return itemService.createItem(userId, itemDto);
    }

    @GetMapping(value = "/{itemId}")
    public ItemDto getItem(@RequestHeader(Variables.USER_ID) Integer userId,
                           @PathVariable Integer itemId) {
        return itemService.getItem(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader(Variables.USER_ID) Integer userId,
                                     @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                     @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        return itemService.getAllItems(userId, from, size);
    }

    @PatchMapping(value = "/{itemId}")
    public ItemDto updateItem(@RequestHeader(Variables.USER_ID) Integer userId,
                              @PathVariable Integer itemId,
                              @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(Variables.USER_ID) Integer userId,
                           @PathVariable Integer itemId) {
        itemService.deleteItem(userId, itemId);
    }

    @GetMapping("search")
    public List<ItemDto> searchItems(@RequestParam String text,
                                     @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                     @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        return itemService.searchItems(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(@RequestHeader(Variables.USER_ID) Integer userId,
                                         @PathVariable Integer itemId,
                                         @RequestBody @Validated CommentRequestDto text) {
        return itemService.addComment(userId, itemId, text);
    }
}


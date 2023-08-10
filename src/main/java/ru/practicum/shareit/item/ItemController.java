package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Variables;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final String pathId = "/{id}";
    private final String pathComment = "/{id}/comment";

    @PostMapping()
    public ItemDto createItem(@RequestHeader(value = Variables.USER_ID) Integer ownerId,
                          @Valid @RequestBody @NotNull ItemDto item) {
        return itemService.createItem(ownerId, item);
    }

    @GetMapping(pathId)
    public ItemDto getItem(@RequestHeader(value = Variables.USER_ID) Integer ownerId,
                           @PathVariable Integer id) {
        return itemService.getItem(ownerId, id);
    }

    @GetMapping()
    public List<ItemDto> getAllItems(@RequestHeader(value = Variables.USER_ID) Integer ownerId) {
        return itemService.getAllItems(ownerId);
    }

    @PatchMapping(pathId)
    public ItemDto updateItem(@RequestHeader(value = Variables.USER_ID) Integer ownerId,
                          @PathVariable Integer id,
                          @Valid @RequestBody @NotNull ItemDto item) {
        return itemService.updateItem(ownerId, id, item);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader(value = Variables.USER_ID) Integer ownerId,
                                  @RequestParam(name = "text") String text) {
        return itemService.searchItems(text);
    }

    @PostMapping(pathComment)
    public CommentDto addComment(@RequestHeader(value = Variables.USER_ID) Integer authorId,
                                 @PathVariable Integer id,
                                 @Valid @RequestBody @NotNull CommentDto commentBody) {
        return itemService.addComment(authorId, id, commentBody);
    }
}
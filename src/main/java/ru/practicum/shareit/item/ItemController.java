package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Variables;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final String pathId = "/{id}";

    @PostMapping()
    public ItemDto createItem(@RequestHeader(value = Variables.USER_ID) Integer ownerId,
                          @Valid @RequestBody ItemDto item) {
        return itemService.createItem(ownerId, item);
    }

    @GetMapping(pathId)
    public ItemDto getItem(@PathVariable Integer id) {
        return itemService.getItem(id);
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
    public List<ItemDto> getItems(@RequestParam(name = "text") String text) {
        return itemService.getItems(text);
    }
}
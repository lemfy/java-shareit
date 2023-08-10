package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getIsAvailable())
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .isAvailable(itemDto.getAvailable())
                .build();
    }

    public static void updateItemWithExistingValues(Item updatedItem, Item existItem) {
        if (updatedItem.getId() == null)
            updatedItem.setId(existItem.getId());
        if (updatedItem.getName() == null)
            updatedItem.setName(existItem.getName());
        if (updatedItem.getDescription() == null)
            updatedItem.setDescription(existItem.getDescription());
        if (updatedItem.getIsAvailable() == null)
            updatedItem.setIsAvailable(existItem.getIsAvailable());
        if (updatedItem.getOwner() == null)
            updatedItem.setOwner(existItem.getOwner());
    }
}
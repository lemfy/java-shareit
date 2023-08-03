package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item createItem(Integer ownerId, Item item);

    Item getItem(Integer id);

    List<Item> getAllItems(Integer ownerId);

    List<Item> getItems(String text);

    Item updateUser(Integer ownerId, Integer id, Item item);
}
package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemStorage {
    Item createItem(Integer ownerId, Item item, User user);

    Item getItem(Integer id);

    List<Item> getAllItems(Integer ownerId, User user);

    List<Item> getItems(String text);

    Item updateItem(Integer ownerId, Integer id, Item item, User user);
}
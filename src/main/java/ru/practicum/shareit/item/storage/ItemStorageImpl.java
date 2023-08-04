package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemStorageImpl implements ItemStorage {
    private final UserStorage userStorage;
    private final Map<Integer, Item> items = new HashMap<>();
    private int count = 1;

    @Override
    public Item createItem(Integer ownerId, Item item, User user) {
        item.setId(count);
        item.setOwner(user);
        items.put(count++, item);
        return item;
    }

    @Override
    public Item getItem(Integer id) {
        return items.get(id);
    }

    @Override
    public List<Item> getAllItems(Integer ownerId, User user) {
        return items.values().stream()
                .filter(item -> item.getOwner().equals(user))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getItems(String text) {
        return items.values().stream()
                .filter(item -> item.getIsAvailable()
                        && !text.isBlank()
                        && (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                ).collect(Collectors.toList());
    }

    @Override
    public Item updateItem(Integer ownerId, Integer id, Item item, User user) {
        Item newItem = items.get(id);
        if (newItem.getOwner().equals(user)) {
            if (item.getDescription() != null && !item.getDescription().isBlank()) {
                newItem.setDescription(item.getDescription());
            }
            if (item.getName() != null && !item.getName().isBlank()) {
                newItem.setName(item.getName());
            }
            if (item.getIsAvailable() != null) {
                newItem.setIsAvailable(item.getIsAvailable());
            }
            items.put(id, newItem);
        }
        return newItem;
    }
}
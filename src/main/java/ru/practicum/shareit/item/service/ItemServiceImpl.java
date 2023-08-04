package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.WrongItemException;
import ru.practicum.shareit.exceptions.WrongOwnerException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto createItem(Integer ownerId, ItemDto itemDto) {
        User user = checkOwner(ownerId);
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            throw new WrongItemException("Необходимо заполнить поле НАЗВАНИЕ");
        } else if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            throw new WrongItemException("Необходимо заполнить поле ОПИСАНИЕ");
        } else if (itemDto.getAvailable() == null) {
            throw new WrongItemException("Необходимо заполнить поле СТАТУС");
        } else {
            Item item = ItemMapper.toItem(itemDto);
            Item newItem = itemStorage.createItem(ownerId, item, user);
            return ItemMapper.toItemDto(newItem);
        }
    }

    @Override
    public ItemDto getItem(Integer itemId) {
        Item newItem = itemStorage.getItem(itemId);
        if (newItem == null) {
            throw new WrongItemException("Предмет не найден");
        }
        return ItemMapper.toItemDto(newItem);
    }

    @Override
    public List<ItemDto> getAllItems(Integer ownerId) {
        User user = checkOwner(ownerId);
        return ItemMapper.toItemDtoList(itemStorage.getAllItems(ownerId, user));
    }

    @Override
    public ItemDto updateItem(Integer ownerId, Integer itemId, ItemDto itemDto) {
        User user = checkOwner(ownerId);
        Item oldItem = itemStorage.getItem(itemId);
        if (oldItem == null) {
            throw new WrongItemException("Предмет не найден");
        }
        if (oldItem.getOwner().getId().equals(ownerId)) {
            Item item = ItemMapper.toItem(itemDto);
            Item newItem = itemStorage.updateItem(ownerId, itemId, item, user);
            return ItemMapper.toItemDto(newItem);
        } else {
            throw new WrongOwnerException("Пользователь с id {} не найден", ownerId);
        }
    }

    @Override
    public List<ItemDto> getItems(String text) {
        return ItemMapper.toItemDtoList(itemStorage.getItems(text));
    }

    private User checkOwner(Integer ownerId) {
        User user = userStorage.getUser(ownerId);
        if (user == null) {
            throw new WrongOwnerException("Пользователь с id {} не найден", ownerId);
        }
        return user;
    }
}
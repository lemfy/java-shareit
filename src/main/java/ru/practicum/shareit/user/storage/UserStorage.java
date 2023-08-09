package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User createUser(User user);

    User getUser(Integer id);

    List<User> getAllUsers();

    void updateUser(Integer id, User user);

    void deleteUser(Integer id);

    User getUserByEmail(String email);
}
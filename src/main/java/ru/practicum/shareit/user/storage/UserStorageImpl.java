package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.WrongUserException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserStorageImpl implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int count = 1;

    @Override
    public User createUser(User user) {
        user.setId(count);
        users.put(user.getId(), user);
        count++;
        return user;
    }

    @Override
    public User getUser(Integer id) {
        return users.getOrDefault(id, null);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void updateUser(Integer id, User user) {
        if (users.containsKey(id)) {
            User existsUser = users.get(id);
            if (user.getEmail() != null) {
                existsUser.setEmail(user.getEmail());
            }
            if (user.getName() != null) {
                existsUser.setName(user.getName());
            }
        }
    }

    @Override
    public void deleteUser(Integer id) {
        if (users.containsKey(id)) {
            users.remove(id);
        } else {
            throw new WrongUserException("Пользователь не найден");
        }
    }

    @Override
    public User getUserByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findAny()
                .orElse(null);
    }
}
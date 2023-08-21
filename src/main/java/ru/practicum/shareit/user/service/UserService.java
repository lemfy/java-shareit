package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    UserDto addUser(UserDto userDto);

    UserDto updateUser(Long id, UserDto updatedUser);

    void removeUser(Long id);
}

package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto user);

    UserDto getUser(Integer id);

    List<UserDto> getAllUsers();

    UserDto updateUser(Integer id, UserDto user);

    void deleteUser(Integer userId);
}
package ru.practicum.shareit.user.service;

import ru.practicum.shareit.exceptions.WrongUserException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto user) throws WrongUserException;

    UserDto getUser(Integer id);

    List<UserDto> getAllUsers();

    UserDto updateUser(Integer id, UserDto user);

    void deleteUser(Integer userId);
}
package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.exceptions.WrongUserException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    public UserDto createUser(UserDto userDto) {
        User userByEmail = userStorage.getUserByEmail(userDto.getEmail());
        if (userByEmail == null) {
            User user = UserMapper.toUserModel(userDto);
            User newUser = userStorage.createUser(user);
            return UserMapper.toUserDto(newUser);
        } else {
            throw new WrongUserException("Такой email уже зарегистрирован");
        }
    }

    public UserDto getUser(Integer id) {
        User user = userStorage.getUser(id);
        if (user != null) {
            return UserMapper.toUserDto(user);
        } else {
            throw new WrongUserException("Пользователь не найден");
        }
    }

    public List<UserDto> getAllUsers() {
        return UserMapper.toUserDtoList(userStorage.getAllUsers());
    }

    public UserDto updateUser(Integer id, UserDto userDto) {
        User userByEmail = userStorage.getUserByEmail(userDto.getEmail());
        if (userByEmail == null || userByEmail.getId().equals(id)) {
            User user = UserMapper.toUserModel(userDto);
            userStorage.updateUser(id, user);
            User newUser = userStorage.getUser(id);
            return UserMapper.toUserDto(newUser);
        } else {
            throw new WrongUserException("Такой email уже зарегистрирован");
        }
    }

    public void deleteUser(Integer userId) {
        if (userId <= 0) {
            throw new UserNotFoundException("Id пользователя должен быть положительным");
        }
        userStorage.deleteUser(userId);
    }
}
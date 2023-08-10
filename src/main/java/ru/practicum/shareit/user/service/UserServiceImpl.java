package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.exceptions.WrongOwnerException;
import ru.practicum.shareit.exceptions.WrongUserException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserDto createUser(UserDto userDto) throws WrongUserException {
        User user = UserMapper.toUser(userDto);
        User newUser = userRepository.save(user);
        return UserMapper.toUserDto(newUser);
    }

    public UserDto getUser(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return UserMapper.toUserDto(user.get());
        } else {
            throw new WrongOwnerException("Пользователь с id {} не найден", id);
        }
    }

    public List<UserDto> getAllUsers() {
        return UserMapper.toUserDtoList(userRepository.findAll());
    }

    public UserDto updateUser(Integer id, UserDto userDto) {
        User userByEmail = null;
        if (userDto.getEmail() != null) {
            userByEmail = userRepository.findByEmail(userDto.getEmail());
        }
        if (userByEmail == null || userByEmail.getId().equals(id)) {
            User user = userRepository.findById(id).get();
            if (userDto.getName() != null) {
                user.setName(userDto.getName());
            }
            if (userDto.getEmail() != null) {
                user.setEmail(userDto.getEmail());
            }
            userRepository.save(user);
            User newUser = userRepository.findById(id).get();
            return UserMapper.toUserDto(newUser);
        } else {
            throw new WrongUserException("Такой email уже зарегистрирован");
        }
    }

    public void deleteUser(Integer userId) {
        if (userId <= 0) {
            throw new UserNotFoundException("Id пользователя должен быть положительным");
        }
        userRepository.deleteById(userId);
    }
}
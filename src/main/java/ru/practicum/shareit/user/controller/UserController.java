package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;
    private final String pathId = "/{id}";

    @PostMapping
    public UserDto createUser(@RequestBody @Validated UserDto userDto) {
        return userService.createUser(userDto);
    }

    @GetMapping(pathId)
    public UserDto getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PatchMapping(pathId)
    public UserDto updateUser(@PathVariable Integer id,
                              @RequestBody UserDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping(pathId)
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
}
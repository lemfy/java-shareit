package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final String pathId = "/{id}";

    @PostMapping()
    public UserDto createUser(@Valid @RequestBody UserDto user) {
        return userService.createUser(user);
    }

    @GetMapping(pathId)
    public UserDto getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @GetMapping()
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PatchMapping(pathId)
    public UserDto updateUser(@PathVariable Integer id, @RequestBody @NotNull UserDto user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping(pathId)
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
}
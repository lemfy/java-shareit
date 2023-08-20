package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {
    private final UserService userService;
    private UserDto userDto;
    private UserDto userDto2;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .name("user1")
                .email("user1@user.com")
                .build();

        userDto2 = UserDto.builder()
                .name("user2")
                .email("user2@user.com")
                .build();
    }

    @Test
    void addUserReturnUserTest() {
        UserDto actualUserDto = userService.createUser(userDto);
        Assertions.assertEquals(userDto.getName(), actualUserDto.getName(), "Name не совпадают");
        Assertions.assertEquals(userDto.getEmail(), actualUserDto.getEmail(), "Email не совпадают");
    }

    @Test
    void getUserByIdReturnUserTest() {
        Integer userId = userService.createUser(userDto).getId();
        UserDto actualUserDto = userService.getUser(userId);

        Assertions.assertEquals(userDto.getName(), actualUserDto.getName(), "Name не совпадают");
        Assertions.assertEquals(userDto.getEmail(), actualUserDto.getEmail(), "Email не совпадают");
    }

    @Test
    void getUserByIdReturnUserNotFoundExceptionTest() {
        userService.createUser(userDto);
        assertThrows(UserNotFoundException.class, () -> userService.getUser(999));
    }

    @Test
    void getAllUsersReturnUsersTest() {
        userService.createUser(userDto);
        userService.createUser(userDto2);
        Assertions.assertEquals(2, userService.getAllUsers().size(), "Количество пользователей не совпадает");
    }

    @Test
    void getAllUsersReturnEmptyListTest() {
        Assertions.assertEquals(0, userService.getAllUsers().size(), "Количество пользователей не совпадает");
    }

    @Test
    void updateUserReturnUpdatedUserTest() {
        Integer id = userService.createUser(userDto).getId();
        UserDto updatedUserDto = UserDto.builder()
                .name("User updated name")
                .email("updated@test.com")
                .build();
        Assertions.assertEquals(updatedUserDto.getName(), userService.updateUser(id, updatedUserDto).getName(),
                "Данные пользователя не совпадают");
    }

    @Test
    void removeUserTest() {
        Integer id = userService.createUser(userDto).getId();
        Assertions.assertEquals(1, userService.getAllUsers().size(), "Количество пользователей не совпадает");
        userService.deleteUser(id);
        Assertions.assertEquals(0, userService.getAllUsers().size(), "Количество пользователей не совпадает");
    }
}
package ru.practicum.shareit.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.user.mapper.UserMapper.toUser;
import static ru.practicum.shareit.user.mapper.UserMapper.toUserDto;

class UserMapperTest {

    @Test
    void toUserDtoTest() {
        User user = User.builder()
                .id(1L)
                .name("Test user name")
                .email("test@test.com")
                .build();
        UserDto userDto = toUserDto(user);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    void toUserTest() {
        UserDto userDto = UserDto.builder()
                .name("Test user name")
                .email("test@test.com")
                .build();
        User user = toUser(userDto);
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
    }

    @Test
    void userEqualsTest() {
        User user1 = User.builder()
                .id(1L)
                .name("Test user name")
                .email("test@test.com")
                .build();

        User user2 = User.builder()
                .id(1L)
                .name("Test user name")
                .email("test@test.com")
                .build();
        assertEquals(user1,user2);
    }

}


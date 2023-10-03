package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

	private final UserClient userClient;

	@PostMapping
	public ResponseEntity<Object> createUser(@RequestBody @Valid UserDto userDto) {
		log.info("Create new user: {}", userDto);
		return userClient.createUser(userDto);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Object> getUser(@PathVariable Long id) {
		log.info("Get user by id = {}", id);
		return userClient.getUser(id);
	}

	@GetMapping
	public ResponseEntity<Object> getAllUsers() {
		log.info("Get all users");
		return userClient.getAllUsers();
	}

	@PatchMapping(value = "/{id}")
	public ResponseEntity<Object> updateUser(@PathVariable Long id,
							  @RequestBody UserDto userDto) {
		log.info("Update user with id = {} : {}", id, userDto);
		return userClient.updateUser(id, userDto);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
		log.info("Delete user with id = {}", id);
		return userClient.deleteUser(id);
	}
}
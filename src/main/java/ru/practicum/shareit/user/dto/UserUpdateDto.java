package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserUpdateDto {
    private Long id;
    private String name;
    @Email
    private String email;
}
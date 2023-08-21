package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserUpdateDto {
    private Long id;
    @NotBlank
    private String name;
    @Email
    private String email;
}
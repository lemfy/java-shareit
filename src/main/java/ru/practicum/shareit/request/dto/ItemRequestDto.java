package ru.practicum.shareit.request.dto;


import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemRequestDto {
    @NotNull
    private String description;
}
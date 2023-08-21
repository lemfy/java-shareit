package ru.practicum.shareit.request.dto;


import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemRequestDto {
    @NotNull
    private String description;
}
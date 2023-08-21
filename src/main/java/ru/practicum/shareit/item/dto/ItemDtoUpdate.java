package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemDtoUpdate {
    @Size(max = 256)
    private String name;
    @Size(max = 512)
    private String description;
    private Boolean available;
    private Long requestId;
}
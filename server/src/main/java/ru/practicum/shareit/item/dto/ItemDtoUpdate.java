package ru.practicum.shareit.item.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemDtoUpdate {
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
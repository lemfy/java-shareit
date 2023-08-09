package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {
    private Integer id;
    private String name;
    private String description;
    private Boolean isAvailable;
    private Integer requestId;
    private User owner;
}
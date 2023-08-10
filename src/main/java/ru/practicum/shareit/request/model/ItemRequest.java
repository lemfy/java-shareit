package ru.practicum.shareit.request.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.persistence.Column;
import java.util.Date;

@Data
public class ItemRequest {
    private Integer id;
    private String description;
    @Column(name = "user_id")
    private User requestor;
    @Column(name = "date_create")
    private Date created;
}
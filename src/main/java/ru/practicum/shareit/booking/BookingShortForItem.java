package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BookingShortForItem {
    private Integer id;
    private Integer bookerId;
    private Integer itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
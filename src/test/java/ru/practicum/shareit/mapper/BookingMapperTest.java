package ru.practicum.shareit.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.booking.mapper.BookingMapper.toBooking;
import static ru.practicum.shareit.booking.mapper.BookingMapper.toBookingDto;

class BookingMapperTest {

    @Test
    void toBookingDtoTest() {
        Item item = Item.builder()
                .name("Test item name")
                .description("Test item description")
                .isAvailable(true)
                .build();
        User booker = User.builder()
                .name("Test user name")
                .email("test@test.com")
                .build();

        Booking booking = Booking.builder()
                .id(1)
                .start(LocalDateTime.of(2022, 10, 22, 10,0, 5))
                .end(LocalDateTime.of(2022, 10, 25, 11,30, 5))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build();

        BookingResponseDto bookingResponseDto = toBookingDto(booking);
        assertEquals(booking.getId(), bookingResponseDto.getId());
        assertEquals(booking.getStart(), bookingResponseDto.getStart());
        assertEquals(booking.getEnd(), bookingResponseDto.getEnd());
        assertEquals(booking.getItem().getName(), bookingResponseDto.getItem().getName());
        assertEquals(booking.getBooker().getName(), bookingResponseDto.getBooker().getName());
        assertEquals(booking.getStatus(), bookingResponseDto.getStatus());
    }

    @Test
    void toBookingTest() {
        BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
                .start(LocalDateTime.of(2022, 10, 22, 10,0, 5))
                .end(LocalDateTime.of(2022, 10, 25, 11,30, 5))
                .build();

        Booking booking = toBooking(bookingRequestDto);
        assertEquals(bookingRequestDto.getStart(), booking.getStart());
        assertEquals(bookingRequestDto.getEnd(), booking.getEnd());
    }

    @Test
    void bookingEqualsTest() {
        Booking booking1 = Booking.builder()
                .id(1)
                .start(LocalDateTime.of(2022, 10, 22, 10,0, 5))
                .end(LocalDateTime.of(2022, 10, 25, 11,30, 5))
                .status(BookingStatus.APPROVED)
                .build();
        Booking booking2 = Booking.builder()
                .id(1)
                .start(LocalDateTime.of(2022, 10, 22, 10,0, 5))
                .end(LocalDateTime.of(2022, 10, 25, 11,30, 5))
                .status(BookingStatus.APPROVED)
                .build();
        assertEquals(booking1,booking2);
    }
}
package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.enums.BookingRequestStatus;
import ru.practicum.shareit.booking.service.search.*;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.mapper.BookingMapper.toBooking;
import static ru.practicum.shareit.booking.mapper.BookingMapper.toBookingDto;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Override
    public BookingResponseDto addNewBooking(Long userId, BookingRequestDto bookingRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Item item = itemRepository.findById(bookingRequestDto.getItemId())
                .orElseThrow(() -> new ItemNotFoundException(bookingRequestDto.getItemId()));
        if (userId.equals(item.getOwner().getId()))
            throw new EntityNotFoundException("Нельзя забронировать свою вешь!");
        if (Boolean.FALSE.equals(item.getIsAvailable()))
            throw new ValidationException(String.format("Вещь с id [%d] недоступна!", item.getId()));
        if (!bookingRequestDto.getEnd().isAfter(bookingRequestDto.getStart()))
            throw new ValidationException("Даты бронирования указаны некорректно!");

        Booking booking = toBooking(bookingRequestDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        return toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto approveBooking(Long userId, Long bookingId, Boolean isApproved) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Бронирование с id [%d] не найдено!", bookingId)));
        if (!Objects.equals(booking.getItem().getOwner().getId(), userId))
            throw new EntityNotFoundException(
                    String.format("Изменение статуса бронирования для пользователя с id [%d] запрещено!", userId));

        if ((isApproved && booking.getStatus().equals(BookingStatus.APPROVED)) ||
                (!isApproved && booking.getStatus().equals(BookingStatus.REJECTED)))
            throw new ValidationException("Статусы бронирования совпадают!");

        booking.setStatus(Boolean.TRUE.equals(isApproved) ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto getBookingById(Long userId, Long bookingId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Бронирование с id [%d] не найдено!", bookingId)));
        if (!Objects.equals(booking.getBooker().getId(), userId) &&
                !Objects.equals(booking.getItem().getOwner().getId(), userId))
            throw new EntityNotFoundException("Доступ к информации о бронировании запрещён!");
        return toBookingDto(booking);
    }

    @Override
    public List<BookingResponseDto> getAllBookings(Long userId, String state, int from, int size) {
        if (from < 0 || size <= 0) {
            throw new ValidationException("from должно быть положительным, size больше 0");
        }
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
        List<Booking> bookings = BookingSearcher.link(
                new BookerIdAndEndIsBeforeOrderByStartDescHandler(bookingRepository),
                new BookerIdAndStartIsAfterOrderByStartDescHandler(bookingRepository),
                new BookerIdAndStartIsBeforeAndEndIsAfterOrderByStartAscHandler(bookingRepository),
                new BookerIdAndStatusRejectedOrderByStartDescHandler(bookingRepository),
                new BookerIdAndStatusWaitingOrderByStartDescHandler(bookingRepository),
                new BookerIdOrderByStartDescHandler(bookingRepository)
        ).find(BookingRequestStatus.getStatus(state), userId, pageable);

        return bookings
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getAllBookingsForOwner(Long userId, String state, int from, int size) {
        if (from < 0 || size <= 0) {
            throw new ValidationException("from должно быть положительным, size больше 0");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
        List<Booking> bookings = BookingSearcher.link(
                new ItemOwnerIdAndEndIsBeforeOrderByStartDescHandler(bookingRepository),
                new ItemOwnerIdAndStartIsAfterOrderByStartDescHandler(bookingRepository),
                new ItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDescHandler(bookingRepository),
                new ItemOwnerIdAndStatusWaitingOrderByStartDescHandler(bookingRepository),
                new ItemOwnerIdAndStatusRejectedOrderByStartDescHandler(bookingRepository),
                new ItemOwnerIdOrderByStartDescHandler(bookingRepository)
        ).find(BookingRequestStatus.getStatus(state), userId, pageable);

        return bookings
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

}


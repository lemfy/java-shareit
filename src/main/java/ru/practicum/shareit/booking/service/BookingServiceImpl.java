package ru.practicum.shareit.booking.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exceptions.BookingNotFoundException;
import ru.practicum.shareit.exceptions.WrongBookingParameterException;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.exceptions.WrongOwnerException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.Date;
import java.util.Optional;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto getBooking(Integer bookerId, Integer id) {
        Optional<Booking> bookingOption = bookingRepository.findById(id);
        if (bookingOption.isPresent()) {
            Booking booking = bookingOption.get();
            if (booking.getBooker().getId().equals(bookerId) || booking.getItem().getOwner().getId().equals(bookerId)) {
                return BookingMapper.toBookingDto(booking);
            } else {
                throw new BookingNotFoundException("Неверные параметры");
            }
        } else {
            throw new BookingNotFoundException("Брони с таким id нет");
        }
    }

    @Override
    public BookingDto booking(Integer bookerId, BookingDto bookingDto) {
        checkDates(bookingDto);

        Optional<Item> item = itemRepository.findById(bookingDto.getItemId());
        if (item.isPresent() && item.get().getIsAvailable()) {
            if (item.get().getOwner().getId().equals(bookerId)) {
                throw new WrongOwnerException("Неверные параметры", bookerId);
            }

            Booking booking = new Booking();

            Optional<User> user = userRepository.findById(bookerId);
            if (user.isPresent()) {
                booking.setBooker(user.get());
            } else {
                throw new UserNotFoundException("Пользователь не найден");
            }
            booking.setDateBegin(bookingDto.getStart());
            booking.setDateEnd(bookingDto.getEnd());
            booking.setItem(item.get());
            booking.setStatus("WAITING");

            bookingRepository.save(booking);

            return BookingMapper.toBookingDto(booking);

        } else if (!item.isPresent()) {
            throw new WrongOwnerException("Вещи с таким id нет", bookerId);
        } else {
            throw new WrongBookingParameterException("Вещь недоступна");
        }
    }

    @Override
    public BookingDto approve(Integer ownerId, Integer bookingId, boolean approved) {
        Optional<Booking> bookingOption = bookingRepository.findById(bookingId);
        if (bookingOption.isPresent()) {
            Item item = bookingOption.get().getItem();
            User owner = item.getOwner();
            if (owner.getId().equals(ownerId)) {
                Booking booking = bookingOption.get();
                if (booking.getStatus().equals("APPROVED")) {
                    throw new WrongBookingParameterException("Неверные параметры");
                }
                String status;
                if (approved) {
                    status = "APPROVED";
                } else {
                    status = "REJECTED";
                }
                booking.setStatus(status);

                bookingRepository.save(booking);
                return BookingMapper.toBookingDto(booking);
            } else {
                throw new BookingNotFoundException("Неверные параметры");
            }
        } else {
            throw new BookingNotFoundException("Брони с такой ID нет");
        }
    }

    private void checkDates(BookingDto bookingDto) {
        if (bookingDto.getEnd().before(bookingDto.getStart())
                || bookingDto.getEnd().before(new Date())
                || bookingDto.getStart().before(new Date())
        ) {
            throw new WrongBookingParameterException("Неверные параметры");
        }
    }

    @Override
    public List<BookingDto> getBooking(String state, Integer bookerId) {
        Optional<User> user = userRepository.findById(bookerId);
        if (user.isPresent()) {
            List<Booking> bookingList = bookingRepository.findByBooker(user.get());
            List<Booking> list = new ArrayList<>();
            if (state.equals("CURRENT")) {
                list = bookingList.stream()
                        .filter(booking -> booking.getDateBegin().before(new Date()))
                        .filter(booking -> booking.getDateEnd().after(new Date()))
                        .sorted(Comparator.comparing(Booking::getDateBegin).reversed())
                        .collect(Collectors.toList());
            } else if (state.equals("REJECTED")) {
                list = bookingList.stream()
                        .filter(booking -> booking.getStatus().equals("REJECTED"))
                        .sorted(Comparator.comparing(Booking::getDateBegin).reversed())
                        .collect(Collectors.toList());
            } else if (state.equals("WAITING")) {
                list = bookingList.stream()
                        .filter(booking -> booking.getStatus().equals("WAITING"))
                        .sorted(Comparator.comparing(Booking::getDateBegin).reversed())
                        .collect(Collectors.toList());
            } else if (state.equals("PAST")) {
                list = bookingList.stream()
                        .filter(booking -> booking.getDateBegin().before(new Date()))
                        .filter(booking -> booking.getDateEnd().before(new Date()))
                        .sorted(Comparator.comparing(Booking::getDateBegin).reversed())
                        .collect(Collectors.toList());
            } else if (state.equals("FUTURE")) {
                list = bookingList.stream()
                        .filter(booking -> booking.getDateBegin().after(new Date()) && booking.getDateEnd().after(new Date()))
                        .sorted(Comparator.comparing(Booking::getDateBegin).reversed())
                        .collect(Collectors.toList());
            } else {
                list = bookingList.stream()
                        .sorted(Comparator.comparing(Booking::getDateBegin).reversed())
                        .collect(Collectors.toList());
            }
            return BookingMapper.toBookingDtoList(list);
        } else {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    @Override
    public List<BookingDto> ownerItemsBooking(String state, Integer ownerId) {
        Optional<User> user = userRepository.findById(ownerId);
        if (user.isPresent()) {
            List<Item> ownerItemList = itemRepository.findByOwner(user.get());
            List<Booking> bookingList = new ArrayList<>();
            ownerItemList.stream().forEach(item -> {
                        List<Booking> itemBookingList = bookingRepository.findByItem(item);
                        bookingList.addAll(itemBookingList);
                    }
            );
            List<Booking> list = new ArrayList<>();
            if (state.equals("CURRENT")) {
                list = bookingList.stream()
                        .filter(booking -> booking.getDateBegin().before(new Date()))
                        .filter(booking -> booking.getDateEnd().after(new Date()))
                        .sorted(Comparator.comparing(Booking::getDateBegin).reversed())
                        .collect(Collectors.toList());
            } else if (state.equals("REJECTED")) {
                list = bookingList.stream()
                        .filter(booking -> booking.getStatus().equals("REJECTED"))
                        .sorted(Comparator.comparing(Booking::getDateBegin).reversed())
                        .collect(Collectors.toList());
            } else if (state.equals("WAITING")) {
                list = bookingList.stream()
                        .filter(booking -> booking.getStatus().equals("WAITING"))
                        .sorted(Comparator.comparing(Booking::getDateBegin).reversed())
                        .collect(Collectors.toList());
            } else if (state.equals("PAST")) {
                list = bookingList.stream()
                        .filter(booking -> booking.getDateBegin().before(new Date()))
                        .filter(booking -> booking.getDateEnd().before(new Date()))
                        .sorted(Comparator.comparing(Booking::getDateBegin).reversed())
                        .collect(Collectors.toList());
            } else if (state.equals("FUTURE")) {
                list = bookingList.stream()
                        .filter(booking -> booking.getDateBegin().after(new Date()) && booking.getDateEnd().after(new Date()))
                        .sorted(Comparator.comparing(Booking::getDateBegin).reversed())
                        .collect(Collectors.toList());
            } else {
                list = bookingList.stream()
                        .sorted(Comparator.comparing(Booking::getDateBegin).reversed())
                        .collect(Collectors.toList());
            }
            return BookingMapper.toBookingDtoList(list);
        } else {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }
}
package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingShortForItem;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.mapper.CommentMapper.toCommentDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItem;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDto;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto createItem(Integer userId, ItemDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Item item = toItem(itemDto);
        item.setOwner(user);
        return toItemDto(itemRepository.save(item));
    }

    public ItemDto getItem(Integer userId, Integer itemId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));
        ItemDto oneMoreItem = toItemDto(item);
        if (userId.equals(item.getOwner().getId())) {
            List<BookingShortForItem> bookings = bookingRepository.findByItemAndStatus(item, BookingStatus.APPROVED);
            oneMoreItem.setLastBooking(findLastBookingForItem(item, bookings));
            oneMoreItem.setNextBooking(findNextBookingForItem(item, bookings));
        }
        oneMoreItem.setComments(getCommentsByItem(item));
        return oneMoreItem;
    }

    public List<ItemDto> getAllItems(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        List<Item> items = itemRepository.findByOwnerOrderByIdAsc(user);
        List<BookingShortForItem> bookings = bookingRepository.findByItemInAndStatus(items, BookingStatus.APPROVED);
        List<ItemDto> oneMoreItems = items.stream()
                .map(item -> {
                            ItemDto itemDto = toItemDto(item);
                            itemDto.setLastBooking(findLastBookingForItem(item, bookings));
                            itemDto.setNextBooking(findNextBookingForItem(item, bookings));
                            itemDto.setComments(getCommentsByItem(item));
                            return itemDto;
                        }
                )
                .collect(Collectors.toList());
        return oneMoreItems;
    }

    @Override
    public ItemDto updateItem(Integer id, Integer itemId, ItemDto itemDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));
        if (item.getOwner() != null && !id.equals(item.getOwner().getId()))
            throw new EntityNotFoundException("Ошибка обновления итема: " + item.getId());
        Item updatedItem = toItem(itemDto);
        ItemMapper.updateItemWithExistingValues(updatedItem, item);
        return toItemDto(itemRepository.save(updatedItem));
    }

    @Override
    public void deleteItem(Integer userId, Integer itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));
        if (item.getOwner() != null && !userId.equals(item.getOwner().getId()))
            throw new EntityNotFoundException("Ошибка удаления итема: " + item.getId()
                    + " пользователем: " + userId);
        itemRepository.deleteById(item.getId());
    }

    public List<ItemDto> searchItems(String text) {
        if (text.isBlank())
            return Collections.emptyList();
        return itemRepository.search(text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public CommentResponseDto addComment(Integer userId, Integer itemId, CommentRequestDto text) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));
        BookingShortForItem booking = bookingRepository.findFirstByItemAndBookerAndStatus(item, user, BookingStatus.APPROVED);
        if (booking == null)
            throw new ValidationException("Ошибка создания комментария");
        else if (booking.getEnd().isAfter(LocalDateTime.now()))
            throw new ValidationException("Ошибка создания бронирования");
        Comment comment = CommentMapper.toComment(text, item, user);
        commentRepository.save(comment);
        return toCommentDto(comment);
    }

    private List<CommentResponseDto> getCommentsByItem(Item item) {
        return commentRepository.findByItem(item)
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    private BookingShortForItem findLastBookingForItem(Item item, List<BookingShortForItem> bookings) {
        return bookings
                .stream()
                .filter(b -> b.getItemId().equals(item.getId()))
                .filter(b -> b.getStart().isBefore(LocalDateTime.now()))
                .max(Comparator.comparing(BookingShortForItem::getStart))
                .orElse(null);
    }

    private BookingShortForItem findNextBookingForItem(Item item, List<BookingShortForItem> bookings) {
        return bookings
                .stream()
                .filter(b -> b.getItemId().equals(item.getId()))
                .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(BookingShortForItem::getStart))
                .orElse(null);
    }
}
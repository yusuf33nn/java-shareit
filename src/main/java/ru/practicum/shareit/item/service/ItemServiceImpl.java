package ru.practicum.shareit.item.service;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.exception.BusinessLogicException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.CommentRepository;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;
    private final UserService userService;
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional(readOnly = true)
    public ItemWithBookingsDto getItemById(Long itemId) {
        var item = findItemById(itemId);
        var result =
                bookingRepository.getAllByItemIdAndStateIn(itemId, List.of(BookingState.PAST, BookingState.FUTURE))
                        .stream()
                        .collect(Collectors.partitioningBy(e -> e.getState() == BookingState.PAST));
        var pastBookings = result.get(true).stream()
                .sorted(Comparator.comparing(Booking::getEnd))
                .toList();
        var lastBooking = pastBookings.isEmpty() ? null : pastBookings.getLast();

        var futureBookings = result.get(false).stream()
                .sorted(Comparator.comparing(Booking::getStart))
                .toList();
        var nextBooking = futureBookings.isEmpty() ? null : futureBookings.getFirst();

        var itemDto = itemMapper.toItemWithBookingsDto(item);
        itemDto.setLastBooking(lastBooking == null ? null : bookingMapper.toDto(lastBooking));
        itemDto.setNextBooking(nextBooking == null ? null : bookingMapper.toDto(nextBooking));
        itemDto.setComments(commentMapper.toCommentDtoList(item.getComments()));
        return itemDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getAllItems(Long userId) {
        userService.findByUserId(userId);
        return itemMapper.toDtoList(itemRepository.findAllByOwnerId(userId));
    }

    @Override
    @Transactional
    public ItemDto createItem(Long userId, ItemCreateDto itemCreateDto) {
        var owner = userService.findByUserId(userId);
        Item item = itemMapper.toCreateEntity(itemCreateDto);
        item.setOwner(owner);
        return itemMapper.toDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long userId, Long itemId, ItemUpdateDto itemUpdateDto) {
        userService.findByUserId(userId);
        var itemFromDb = findItemById(itemId);
        if (!Objects.equals(itemFromDb.getOwner().getId(), userId)) {
            throw new NotFoundException("Only owner can update item");
        }
        if (itemUpdateDto.getName() != null) itemFromDb.setName(itemUpdateDto.getName());
        if (itemUpdateDto.getDescription() != null) itemFromDb.setDescription(itemUpdateDto.getDescription());
        if (itemUpdateDto.getAvailable() != null) itemFromDb.setAvailable(itemUpdateDto.getAvailable());
        return itemMapper.toDto(itemRepository.save(itemFromDb));
    }

    @Override
    @Transactional
    public CommentDto commentItem(Long userId, Long itemId, CommentCreateDto commentCreateDto) {
        var user = userService.findByUserId(userId);
        var item = findItemById(itemId);
        var result = bookingRepository.getAllByBookerIdAndStateIsIn(userId,
                List.of(BookingState.PAST));
        if (result.isEmpty()) {
            throw new BusinessLogicException("You can't comment item if you didn't book it");
        }
        var comment = Comment.builder()
                .text(commentCreateDto.getText())
                .item(item)
                .authorName(user.getName())
                .build();
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (StringUtils.isBlank(text)) {
            return Collections.emptyList();
        }
        var searchedItems = itemRepository.searchItemsByNameIgnoreCaseOrDescriptionIgnoreCase(text, text)
                .stream()
                .filter(Item::getAvailable)
                .toList();
        return itemMapper.toDtoList(searchedItems);
    }

    @Override
    public Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id %d not found".formatted(itemId)));
    }
}

package ru.practicum.shareit.item.service;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemWithBookingsDto getItemById(Long itemId);

    Item findItemById(Long itemId);

    Page<ItemDto> getAllItems(Long userId, int page, int size);

    ItemDto createItem(Long userId, ItemCreateDto itemCreateDto);

    ItemDto updateItem(Long userId, Long itemId, ItemUpdateDto itemUpdateDto);

    CommentDto commentItem(Long userId, Long itemId, CommentCreateDto commentCreateDto);

    Page<ItemDto> searchItems(String text, int page, int size);

//    List<ItemDto> findAllByRequestId(Long requestId);
}

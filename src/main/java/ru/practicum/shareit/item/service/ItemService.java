package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto getItemById(Long itemId);

    Item findItemById(Long itemId);

    List<ItemDto> getAllItems(Long userId);

    ItemDto createItem(Long userId, ItemCreateDto itemCreateDto);

    ItemDto updateItem(Long userId, Long itemId, ItemUpdateDto itemUpdateDto);

    List<ItemDto> searchItems(String text);
}

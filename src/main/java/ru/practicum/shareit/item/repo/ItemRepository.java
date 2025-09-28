package ru.practicum.shareit.item.repo;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    Item getItemById(Long itemId);

    List<Item> getAllItems(Long ownerId);

    Item createItem(Item item);

    Item updateItem(Item item);

    List<Item> searchItems(String text);
}

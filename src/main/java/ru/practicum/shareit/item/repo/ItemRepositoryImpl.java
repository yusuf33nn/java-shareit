package ru.practicum.shareit.item.repo;

import org.apache.commons.lang3.Strings;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final AtomicLong itemCounter = new AtomicLong();
    private final Map<Long, Item> items =  new HashMap<>();

    @Override
    public Item getItemById(Long itemId) {
        return Optional.ofNullable(items.get(itemId))
                .orElseThrow(() -> new NotFoundException("Item with id %d not found".formatted(itemId)));
    }

    @Override
    public List<Item> getAllItems(Long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwner() ==  ownerId)
                .toList();
    }

    @Override
    public Item createItem(Item item) {
        var itemId = itemCounter.incrementAndGet();
        item.setId(itemId);
        items.put(itemId, item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        getItemById(item.getId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> searchItems(String text) {
        return items.values().stream()
                .filter(item ->
                        (Strings.CI.contains(item.getName(), text) || Strings.CI.contains(item.getDescription(), text))
                                && Boolean.TRUE.equals(item.getAvailable()))
                .toList();
    }
}

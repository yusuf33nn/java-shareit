package ru.practicum.shareit.item.service;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Override
    public ItemDto getItemById(Long itemId) {
        var item = findItemById(itemId);
        return itemMapper.toDto(item);
    }

    @Override
    public List<ItemDto> getAllItems(Long userId) {
        return itemMapper.toDtoList(itemRepository.findAllByOwnerId(userId));
    }

    @Override
    public ItemDto createItem(Long userId, ItemCreateDto itemCreateDto) {
        var owner = userService.findByUserId(userId);
        Item item = itemMapper.toCreateEntity(itemCreateDto);
        item.setOwner(owner);
        return itemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemUpdateDto itemUpdateDto) {
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

    private Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id %d not found".formatted(itemId)));
    }
}

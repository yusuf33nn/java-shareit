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

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final UserService userService;
    private final ItemRepository itemRepository;

    @Override
    public ItemDto getItemById(Long itemId) {
        return ItemMapper.toDto(itemRepository.getItemById(itemId));
    }

    @Override
    public List<ItemDto> getAllItems(Long userId) {
        return ItemMapper.toDtoList(itemRepository.getAllItems(userId));
    }

    @Override
    public ItemDto createItem(Long userId, ItemCreateDto itemCreateDto) {
        userService.getUserById(userId);
        Item item = ItemMapper.toCreateEntity(itemCreateDto);
        item.setOwner(userId);
        return ItemMapper.toDto(itemRepository.createItem(item));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemUpdateDto itemUpdateDto) {
        var itemFromDb = itemRepository.getItemById(itemId);
        if (itemFromDb.getOwner() != userId) {
            throw new NotFoundException("Only owner can update item");
        }
        var item = ItemMapper.toUpdateEntity(itemUpdateDto);
        item.setId(itemId);
        return ItemMapper.toDto(itemRepository.updateItem(item));
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (StringUtils.isBlank(text)) {
            return Collections.emptyList();
        }
        return ItemMapper.toDtoList(itemRepository.searchItems(text));
    }
}

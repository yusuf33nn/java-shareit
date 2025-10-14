package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController implements ItemApi {

    private final ItemService itemService;


    @Override
    public ResponseEntity<ItemWithBookingsDto> getItemById(Long itemId) {
        return ResponseEntity.ok(itemService.getItemById(itemId));
    }

    @Override
    public ResponseEntity<List<ItemDto>> getAllItems(Long userId) {
        return ResponseEntity.ok(itemService.getAllItems(userId));
    }

    @Override
    public ResponseEntity<ItemDto> createItem(Long userId, ItemCreateDto itemCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(itemService.createItem(userId, itemCreateDto));
    }

    @Override
    public ResponseEntity<ItemDto> updateItem(Long userId, Long itemId, ItemUpdateDto itemUpdateDto) {
        return ResponseEntity.ok(itemService.updateItem(userId, itemId, itemUpdateDto));
    }

    @Override
    public ResponseEntity<CommentDto> commentItem(Long userId, Long itemId, CommentCreateDto commentCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(itemService.commentItem(userId, itemId, commentCreateDto));
    }

    @Override
    public ResponseEntity<List<ItemDto>> searchItems(String text) {
        return ResponseEntity.ok(itemService.searchItems(text));
    }
}

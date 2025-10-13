package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;

import java.util.List;

import static ru.practicum.shareit.utils.Constants.USER_HEADER;

@RequestMapping(path = "/items")
public interface ItemApi {

    @GetMapping("/{itemId}")
    ResponseEntity<ItemWithBookingsDto> getItemById(@NotNull @PathVariable("itemId") Long itemId);

    @GetMapping
    ResponseEntity<List<ItemDto>> getAllItems(@RequestHeader(USER_HEADER) Long userId);

    @PostMapping
    ResponseEntity<ItemDto> createItem(@RequestHeader(USER_HEADER) Long userId,
                                       @Valid @RequestBody ItemCreateDto itemCreateDto);

    @PatchMapping("/{itemId}")
    ResponseEntity<ItemDto> updateItem(@RequestHeader(USER_HEADER) Long userId,
                                       @PathVariable("itemId") Long itemId,
                                       @RequestBody ItemUpdateDto itemUpdateDto);

    @PostMapping("/{itemId}/comment")
        ResponseEntity<CommentDto> commentItem(@RequestHeader(USER_HEADER) Long userId,
                                               @PathVariable("itemId") Long itemId,
                                               @Valid @RequestBody CommentCreateDto commentCreateDto);

    @GetMapping("/search")
    ResponseEntity<List<ItemDto>> searchItems(@RequestParam("text") String text);
}

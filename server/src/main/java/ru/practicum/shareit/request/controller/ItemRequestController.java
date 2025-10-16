package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class ItemRequestController implements ItemRequestApi {

    private final ItemRequestService itemRequestService;

    @Override
    public ResponseEntity<ItemRequestDto> createItemRequest(Long userId, ItemRequestCreateDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(itemRequestService.createItemRequest(userId, createDto));
    }

    @Override
    public ResponseEntity<ItemRequestDto> getItemRequestById(Long userId, Long requestId) {
        return ResponseEntity.ok(itemRequestService.getItemRequestById(userId, requestId));
    }

    @Override
    public ResponseEntity<List<ItemRequestDto>> getUsersItemRequests(Long userId, int page, int size) {
        return ResponseEntity.ok(itemRequestService.getUsersItemRequests(userId, page, size));
    }

    @Override
    public ResponseEntity<List<ItemRequestDto>> getOthersItemRequests(Long userId, int page, int size) {
        return ResponseEntity.ok(itemRequestService.getOthersItemRequests(userId, page, size));
    }
}

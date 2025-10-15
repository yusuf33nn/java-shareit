package ru.practicum.request.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.request.ItemRequestClient;
import ru.practicum.request.dto.ItemRequestCreateDto;

import static ru.practicum.utils.Constants.USER_HEADER;
import static ru.practicum.utils.ResponseUtils.createResponse;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader(USER_HEADER) Long userId,
                                                     @Valid @RequestBody ItemRequestCreateDto createDto) {
        var response = itemRequestClient.createItemRequest(userId, createDto);
        return createResponse(response);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader(USER_HEADER) Long userId,
                                                      @PathVariable("requestId") Long requestId) {
        var response = itemRequestClient.getItemRequestById(userId, requestId);
        return createResponse(response);
    }
}

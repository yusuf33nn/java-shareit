package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import static ru.practicum.shareit.utils.Constants.USER_HEADER;

@RequestMapping(path = "/requests")
public interface ItemRequestApi {

    @PostMapping
    ResponseEntity<ItemRequestDto> createItemRequest(@RequestHeader(USER_HEADER) Long userId,
                                                     @Valid @RequestBody ItemRequestCreateDto createDto);

    @GetMapping("/{requestId}")
    ResponseEntity<ItemRequestDto> getItemRequestById(@RequestHeader(USER_HEADER) Long userId,
                                                      @PathVariable("requestId") Long requestId);

    @GetMapping
    ResponseEntity<Page<ItemRequestDto>> getUsersItemRequests(@RequestHeader(USER_HEADER) Long userId,
                                                              @RequestParam(defaultValue = "0") @Min(0) int page,
                                                              @RequestParam(defaultValue = "20") @Min(1) @Max(40) int size);


    @GetMapping
    ResponseEntity<Page<ItemRequestDto>> getOthersItemRequests(@RequestHeader(USER_HEADER) Long userId,
                                                               @RequestParam(defaultValue = "0") @Min(0) int page,
                                                               @RequestParam(defaultValue = "20") @Min(1) @Max(40) int size);


}

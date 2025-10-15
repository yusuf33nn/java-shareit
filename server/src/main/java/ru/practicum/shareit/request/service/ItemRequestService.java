package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Optional;

public interface ItemRequestService {

    ItemRequestDto createItemRequest(Long userId, ItemRequestCreateDto createDto);

    Optional<ItemRequest> findItemRequestById(Long requestId);

    ItemRequestDto getItemRequestById(Long userId, Long requestId);

    Page<ItemRequestDto> getUsersItemRequests(Long userId, int page, int size);

    Page<ItemRequestDto> getOthersItemRequests(Long userId, int page, int size);
}

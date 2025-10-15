package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repo.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserService userService;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    @Transactional
    public ItemRequestDto createItemRequest(Long userId, ItemRequestCreateDto createDto) {
        userService.findByUserId(userId);
        var itemRequest = ItemRequest.builder()
                .description(createDto.getDescription())
                .build();
        return itemRequestMapper.toDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public Optional<ItemRequest> findItemRequestById(Long requestId) {
        return itemRequestRepository.findById(requestId);
    }

    @Override
    public ItemRequestDto getItemRequestById(Long userId, Long requestId) {
        userService.findByUserId(userId);
        var itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NullPointerException("Item request with id %d not found".formatted(requestId)));
        var responseDto = itemRequestMapper.toDto(itemRequest);

        return null;
    }

    @Override
    public Page<ItemRequestDto> getUsersItemRequests(Long userId, int page, int size) {
        return null;
    }

    @Override
    public Page<ItemRequestDto> getOthersItemRequests(Long userId, int page, int size) {
        return null;
    }
}

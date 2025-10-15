package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repo.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserService userService;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    @Transactional
    public ItemRequestDto createItemRequest(Long userId, ItemRequestCreateDto createDto) {
        var requestor = userService.findByUserId(userId);
        var itemRequest = ItemRequest.builder()
                .description(createDto.getDescription())
                .requestor(requestor)
                .build();
        return itemRequestMapper.toDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public Optional<ItemRequest> findItemRequestById(Long requestId) {
        return itemRequestRepository.findById(requestId);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestDto getItemRequestById(Long userId, Long requestId) {
        userService.findByUserId(userId);
        var itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NullPointerException("Item request with id %d not found".formatted(requestId)));
        return itemRequestMapper.toDto(itemRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDto> getUsersItemRequests(Long userId, int page, int size) {
        userService.findByUserId(userId);
        Pageable pageable = PageRequest.of(page, size);
        return itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(userId, pageable)
                .map(itemRequestMapper::toDto)
                .stream().toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDto> getOthersItemRequests(Long userId, int page, int size) {
        userService.findByUserId(userId);
        Pageable pageable = PageRequest.of(page, size);

        return itemRequestRepository.findAllByRequestorIdNotOrderByCreatedDesc(userId, pageable)
                .map(itemRequestMapper::toDto)
                .stream().toList();
    }
}

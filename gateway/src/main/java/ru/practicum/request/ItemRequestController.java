package ru.practicum.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.request.dto.ItemRequestCreateDto;

import static ru.practicum.utils.Constants.USER_HEADER;
import static ru.practicum.utils.ResponseUtils.createResponse;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader(USER_HEADER) Long userId,
                                                    @Valid @RequestBody ItemRequestCreateDto createDto) {
        var response = itemRequestClient.createItemRequest(userId, createDto);
        log.info("Created item request: {}", response.getBody());
        return createResponse(response);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader(USER_HEADER) Long userId,
                                                     @PathVariable("requestId") Long requestId) {
        var response = itemRequestClient.getItemRequestById(userId, requestId);
        return createResponse(response);
    }

    @GetMapping()
    public ResponseEntity<Object> getUsersItemRequests(@RequestHeader(USER_HEADER) Long userId,
                                                       @RequestParam(defaultValue = "0") @Min(0) int page,
                                                       @RequestParam(defaultValue = "10") @Min(1) @Max(20) int size) {
        var response = itemRequestClient.getUsersItemRequests(userId, page, size);
        return createResponse(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getOthersItemRequests(@RequestHeader(USER_HEADER) Long userId,
                                                        @RequestParam(defaultValue = "0") @Min(0) int page,
                                                        @RequestParam(defaultValue = "10") @Min(1) @Max(20) int size) {
        var response = itemRequestClient.getOthersItemRequests(userId, page, size);
        return createResponse(response);
    }
}

package ru.practicum.item;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.item.dto.CommentCreateDto;
import ru.practicum.item.dto.ItemCreateDto;
import ru.practicum.item.dto.ItemUpdateDto;

import static ru.practicum.utils.Constants.USER_HEADER;
import static ru.practicum.utils.ResponseUtils.createResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {

    private final ItemClient itemClient;


    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable("itemId") Long itemId) {
        var response = itemClient.getItemById(itemId);
        return createResponse(response);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItems(@RequestHeader(USER_HEADER) Long userId,
                                              @RequestParam(defaultValue = "0") @Min(0) int page,
                                              @RequestParam(defaultValue = "20") @Min(1) @Max(40) int size) {
        var response = itemClient.getAllItems(userId, page, size);
        return createResponse(response);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(USER_HEADER) Long userId,
                                             @Valid @RequestBody ItemCreateDto itemCreateDto) {
        var response = itemClient.createItem(userId, itemCreateDto);
        return createResponse(response);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(USER_HEADER) Long userId,
                                             @PathVariable("itemId") Long itemId,
                                             @RequestBody ItemUpdateDto itemUpdateDto) {
        var response = itemClient.updateItem(userId, itemId, itemUpdateDto);
        return createResponse(response);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> commentItem(@RequestHeader(USER_HEADER) Long userId,
                                              @PathVariable("itemId") Long itemId,
                                              @Valid @RequestBody CommentCreateDto commentCreateDto) {
        var response = itemClient.commentItem(userId, itemId, commentCreateDto);
        return createResponse(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam("text") String text,
                                              @RequestParam(defaultValue = "0") @Min(0) int page,
                                              @RequestParam(defaultValue = "20") @Min(1) @Max(40) int size) {
        ResponseEntity<Object> response;
        if (StringUtils.isBlank(text)) {
            response = ResponseEntity.ok().build();
        } else {
            response = itemClient.searchItems(text, page, size);
        }
        return createResponse(response);
    }
}

package ru.practicum.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemWithBookingsDto {
    Long id;
    String name;
    String description;
    Boolean available;
    Object lastBooking;
    Object nextBooking;
    List<CommentDto> comments;
}

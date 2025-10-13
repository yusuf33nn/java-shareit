package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "owner", ignore = true)
    Item toCreateEntity(ItemCreateDto itemCreateDto);

    @Mapping(target = "owner", ignore = true)
    Item toUpdateEntity(ItemUpdateDto itemUpdateDto);

    ItemDto toDto(Item item);

    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    @Mapping(target = "comments", ignore = true)
    ItemWithBookingsDto toItemWithBookingsDto(Item item);

    List<ItemDto> toDtoList(List<Item> items);
}

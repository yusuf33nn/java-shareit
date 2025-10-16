package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

;

public interface BookingService {

    BookingDto bookItem(Long bookerId, BookingCreateDto bookingDto);

    Booking findBookingById(Long bookingId);

    BookingDto approveBooking(Long ownerId, Long bookingId, boolean approved);

    BookingDto getBookingInfo(Long userId, Long bookingId);

    List<BookingDto> getBookerAllBookingsByState(Long bookerId, BookingState state, int from, int size);

    List<BookingDto> getOwnerAllBookingsByState(Long ownerId, BookingState state, int from, int size);
}

package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookingController implements BookingApi {

    private final BookingService bookingService;

    @Override
    public ResponseEntity<BookingDto> bookItem(Long userId, BookingCreateDto bookingDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.bookItem(userId, bookingDto));
    }

    @Override
    public ResponseEntity<BookingDto> approveBooking(Long userId, Long bookingId, boolean approved) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.approveBooking(userId, bookingId, approved));
    }

    @Override
    public ResponseEntity<BookingDto> getBookingInfo(Long userId, Long bookingId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.getBookingInfo(userId, bookingId));
    }

    @Override
    public ResponseEntity<List<BookingDto>> getBookerAllBookingsByState(Long userId, String stateParam,
                                                                        Integer from, Integer size) {
        BookingState state = BookingState.from(stateParam);
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.getBookerAllBookingsByState(userId, state, from, size));
    }

    @Override
    public ResponseEntity<List<BookingDto>> getOwnerAllBookingsByState(Long userId, String stateParam,
                                                                       Integer from, Integer size) {
        BookingState state = BookingState.from(stateParam);
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.getOwnerAllBookingsByState(userId, state, from, size));
    }
}

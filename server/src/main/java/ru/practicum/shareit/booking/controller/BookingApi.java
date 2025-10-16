package ru.practicum.shareit.booking.controller;

import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

import static ru.practicum.shareit.utils.Constants.USER_HEADER;

@RequestMapping(path = "/bookings")
public interface BookingApi {

    @PostMapping
    ResponseEntity<BookingDto> bookItem(@RequestHeader(USER_HEADER) Long userId,
                                        @RequestBody BookingCreateDto bookingDto);

    @PatchMapping("/{bookingId}")
    ResponseEntity<BookingDto> approveBooking(@RequestHeader(USER_HEADER) Long userId,
                                              @PathVariable("bookingId") Long bookingId,
                                              @RequestParam(name = "approved") boolean approved);

    @GetMapping("/{bookingId}")
    ResponseEntity<BookingDto> getBookingInfo(@RequestHeader(USER_HEADER) Long userId,
                                              @PathVariable("bookingId") Long bookingId);

    @GetMapping
    ResponseEntity<List<BookingDto>> getBookerAllBookingsByState(@RequestHeader(USER_HEADER) Long userId,
                                                                 @RequestParam(name = "state", defaultValue = "all")
                                                                 String stateParam,
                                                                 @RequestParam(name = "from", defaultValue = "0")
                                                                 Integer from,
                                                                 @Positive
                                                                 @RequestParam(name = "size", defaultValue = "10")
                                                                 Integer size);

    @GetMapping("/owner")
    ResponseEntity<List<BookingDto>> getOwnerAllBookingsByState(@RequestHeader(USER_HEADER) Long userId,
                                                                @RequestParam(name = "state", defaultValue = "all")
                                                                String stateParam,
                                                                @RequestParam(name = "from", defaultValue = "0")
                                                                Integer from,
                                                                @Positive
                                                                @RequestParam(name = "size", defaultValue = "10")
                                                                Integer size);
}

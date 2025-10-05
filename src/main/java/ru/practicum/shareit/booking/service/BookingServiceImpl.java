package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.exception.BusinessLogicException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final UserService userService;
    private final ItemService itemService;
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public BookingDto bookItem(Long bookerId, BookingCreateDto bookingDto) {
        var booker = userService.findByUserId(bookerId);
        var item = itemService.findItemById(bookingDto.getItemId());

        if (!item.getAvailable()) {
            throw new IllegalStateException("Item with id %d is not available".formatted(item.getId()));
        }
        validateBookingDates(bookingDto);

        Booking booking = Booking.builder()
                .state(BookingState.WAITING)
                .booker(booker)
                .item(item)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .build();
        var savedBooking = bookingRepository.save(booking);
        return bookingMapper.toDto(savedBooking);
    }

    @Override
    public Booking findBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id %d not found".formatted(bookingId)));
    }

    private void validateBookingDates(BookingCreateDto bookingDto) {
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new IllegalStateException("Start date is after end date");
        }

        if (bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            throw new IllegalStateException("Start date is equal end date");
        }

        if (bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Start date is in the past");
        }

        if (bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("End date is in the past");
        }
    }

    @Override
    public BookingDto approveBooking(Long ownerId, Long bookingId, boolean approved) {
        var booking = findBookingById(bookingId);
        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new BusinessLogicException("Only owner can approve booking");
        }
        booking.setState(approved ? BookingState.APPROVED : BookingState.REJECTED);
        booking.setUpdatedAt(LocalDateTime.now());
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingInfo(Long userId, Long bookingId) {
        var booking = findBookingById(bookingId);
        log.info("User ID: {}", userId);
        log.info("Booking ID: {}", bookingId);
        log.info("OwnerID: {}", booking.getItem().getOwner().getId());
        log.info("BookerID: {}", booking.getBooker().getId());
        if (!booking.getItem().getOwner().getId().equals(userId) && !booking.getBooker().getId().equals(userId)) {
            throw new BusinessLogicException("Only owner or booker can get booking info");
        }
        return bookingMapper.toDto(booking);
    }

    @Override
    public List<BookingDto> getBookerAllBookingsByState(Long bookerId, BookingState state) {
        userService.findByUserId(bookerId);
        List<BookingState> bookingStatesForSearch;
        if (state == BookingState.ALL) {
            bookingStatesForSearch = List.of(BookingState.APPROVED, BookingState.REJECTED,
                    BookingState.WAITING, BookingState.CURRENT, BookingState.PAST, BookingState.FUTURE);
        } else {
            bookingStatesForSearch = List.of(state);
        }
        var result = bookingRepository.getAllByBookerIdAndStateIsIn(bookerId, bookingStatesForSearch);
        return bookingMapper.toDtoList(result);
    }

    @Override
    public List<BookingDto> getOwnerAllBookingsByState(Long ownerId, BookingState state) {
        userService.findByUserId(ownerId);
        List<BookingState> bookingStatesForSearch;
        if (state == BookingState.ALL) {
            bookingStatesForSearch = List.of(BookingState.APPROVED, BookingState.REJECTED,
                    BookingState.WAITING, BookingState.CURRENT, BookingState.PAST, BookingState.FUTURE);
        } else {
            bookingStatesForSearch = List.of(state);
        }
        var result = bookingRepository.findAllByOwnerIdAndStateIn(ownerId, bookingStatesForSearch);
        return bookingMapper.toDtoList(result);
    }
}

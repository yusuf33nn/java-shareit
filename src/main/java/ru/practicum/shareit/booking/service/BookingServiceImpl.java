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
import ru.practicum.shareit.exception.ApproveModificationException;
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

    private static final List<BookingState> BOOKING_STATES_FOR_SEARCH =
            List.of(BookingState.APPROVED, BookingState.REJECTED,
                    BookingState.WAITING, BookingState.CURRENT,
                    BookingState.PAST, BookingState.FUTURE);

    @Override
    @Transactional
    public BookingDto bookItem(Long bookerId, BookingCreateDto bookingDto) {
        validateBookingDates(bookingDto);
        var booker = userService.findByUserId(bookerId);
        var item = itemService.findItemById(bookingDto.getItemId());
        if (!item.getAvailable()) {
            throw new IllegalStateException("Item with id %d is not available".formatted(item.getId()));
        }

        List<Booking> currentAndComingItemBookings = bookingRepository.findAllByItemIdAndStateInOrderByStart(item.getId(),
                List.of(BookingState.CURRENT, BookingState.FUTURE));
        validateBookingCrossingDates(currentAndComingItemBookings, bookingDto);

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

        if (bookingDto.getStart().isBefore(LocalDateTime.now()) || bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Start date or end date is in the past");
        }
    }

    private void validateBookingCrossingDates(List<Booking> currentAndComingItemBookings, BookingCreateDto bookingDto) {
        if (!currentAndComingItemBookings.isEmpty()) {
            var isCrossingOtherBookings = currentAndComingItemBookings.stream()
                    .anyMatch(booking -> {
                        var isInsideAnotherBooking = bookingDto.getStart().isAfter(booking.getStart())
                                && booking.getEnd().isBefore(booking.getEnd());
                        var isOutsideAnotherBooking = bookingDto.getStart().isBefore(booking.getStart())
                                && bookingDto.getEnd().isAfter(booking.getEnd());
                        return isInsideAnotherBooking || isOutsideAnotherBooking;
                    });
            if (isCrossingOtherBookings) {
                throw new BusinessLogicException("Crossing booking is not available");
            }
        }
    }

    @Override
    @Transactional
    public BookingDto approveBooking(Long ownerId, Long bookingId, boolean approved) {
        try {
            userService.findByUserId(ownerId);
        } catch (NotFoundException ignored) { }
        var booking = findBookingById(bookingId);
        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new ApproveModificationException("Only owner can approve booking");
        }
        booking.setState(approved ? BookingState.APPROVED : BookingState.REJECTED);
        booking.setUpdatedAt(LocalDateTime.now());
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingInfo(Long userId, Long bookingId) {
        userService.findByUserId(userId);
        var booking = findBookingById(bookingId);
        if (!booking.getItem().getOwner().getId().equals(userId) && !booking.getBooker().getId().equals(userId)) {
            throw new BusinessLogicException("Only owner or booker can get booking info");
        }
        return bookingMapper.toDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getBookerAllBookingsByState(Long bookerId, BookingState state) {
        userService.findByUserId(bookerId);
        List<BookingState> bookingStatesForSearch = chooseBookingStatesForSearch(state);
        var result = bookingRepository.getAllByBookerIdAndStateIsIn(bookerId, bookingStatesForSearch);
        return bookingMapper.toDtoList(result);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getOwnerAllBookingsByState(Long ownerId, BookingState state) {
        userService.findByUserId(ownerId);
        List<BookingState> bookingStatesForSearch = chooseBookingStatesForSearch(state);

        var result = bookingRepository.findAllByOwnerIdAndStateIn(ownerId, bookingStatesForSearch);
        return bookingMapper.toDtoList(result);
    }

    private List<BookingState> chooseBookingStatesForSearch(BookingState state) {
        return state == BookingState.ALL ? BOOKING_STATES_FOR_SEARCH : List.of(state);
    }
}

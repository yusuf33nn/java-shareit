package ru.practicum.shareit.booking.model;

public enum BookingState {
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    APPROVED,
    REJECTED,
    ALL;

    public static BookingState from(String stringState) {
        for (BookingState state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknown state: " + stringState);
    }
}

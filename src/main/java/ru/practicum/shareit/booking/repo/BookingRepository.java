package ru.practicum.shareit.booking.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.Collection;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> getAllByBookerIdAndStateIsIn(Long bookerId, List<BookingState> bookingStates);

    List<Booking> getAllByItemIdAndStateIn(Long itemId, List<BookingState> bookingStates);

    @Query("""
              select b
              from Booking b
              join fetch b.item i
              join fetch i.owner o
              where o.id = :ownerId
                and b.state in :states
            """)
    List<Booking> findAllByOwnerIdAndStateIn(@Param("ownerId") Long ownerId,
                                             @Param("states") Collection<BookingState> states);
}

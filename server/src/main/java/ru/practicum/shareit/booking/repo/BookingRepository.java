package ru.practicum.shareit.booking.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("""
              select b
              from Booking b
              join fetch b.booker boo
              where boo.id = :bookerId
                and b.state in :states
              order by b.createdAt desc
            """)
    Page<Booking> getAllByBookerIdAndStateIsIn(@Param("bookerId") Long bookerId,
                                               @Param("states") Collection<BookingState> states,
                                               Pageable pageable);

    List<Booking> getAllByItemIdAndStateIn(Long itemId, List<BookingState> bookingStates);

    List<Booking> findAllByBookerId(Long bookerId);

    @Query("""
              select b
              from Booking b
              join fetch b.item i
              join fetch i.owner o
              where o.id = :ownerId
                and b.state in :states
              order by b.createdAt desc
            """)
    Page<Booking> findAllByOwnerIdAndStateIn(@Param("ownerId") Long ownerId,
                                             @Param("states") Collection<BookingState> states,
                                             Pageable pageable);

    List<Booking> findAllByItemIdAndStateInOrderByStart(Long itemId, List<BookingState> bookingStates);
}

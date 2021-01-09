package adelina.luxtravel.repository;

import adelina.luxtravel.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Represents booking's repository
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    /**
     * Retrieves user's bookings
     *
     * @param username user's username
     * @return list of user's bookings
     */
    @Query(value = "SELECT * " +
            "FROM bookings " +
            "WHERE user_id IN (" +
            "SELECT id FROM users WHERE username = ?1) ",
            nativeQuery = true)
    List<Booking> findAllUserBookings(String username);

    /**
     * Updates reserved tickets count for a particular booking
     *
     * @param reservedTicketsCount the new tickets count
     * @param id                   booking's id
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = "UPDATE bookings " +
            "SET reserved_tickets_count = ?1 " +
            "WHERE id = ?2 ",
            nativeQuery = true)
    void updateReservedTicketsCount(int reservedTicketsCount, long id);

    /**
     * Sets date and timestamp of a booking
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = "  UPDATE bookings " +
            "SET reservation_date=CURRENT_TIMESTAMP() " +
            "WHERE id = (SELECT " +
            "id FROM (select * from bookings) " +
            "AS bookings_ ORDER BY " +
            "id desc limit 1 ) ", nativeQuery = true)
    void addReservationDate();
}
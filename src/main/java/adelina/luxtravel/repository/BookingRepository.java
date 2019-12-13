package adelina.luxtravel.repository;

import adelina.luxtravel.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(value = "SELECT count_tickets, user.username," +
                    "booking_data.from_date, booking_data.to_date, booking_data.price " +
                   "FROM booking, user, booking_data" +
                   "WHERE user_id IN (" +
                   "SELECT id FROM user WHERE username = ?1" +
                   "AND booking_data_id = booking_data.id",
            nativeQuery = true)
    List<Booking> findAllUserBookings(String username);

    @Modifying
    @Query(value = "UPDATE booking" +
                   "SET count_tickets= ?1" +
                   "WHERE id = ?2",
            nativeQuery = true)
    void updateByTickets(int ticketsCount, long id);
}
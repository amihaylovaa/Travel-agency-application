package adelina.luxtravel.repository;

import adelina.luxtravel.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(value =  "SELECT * "+
                    "FROM booking, user, traveling_data " +
                    "WHERE user_id IN (" +
                    "SELECT id FROM user WHERE username = ?1) " +
                    "AND traveling_data_id = traveling_data.id",
            nativeQuery = true)
    List<Booking> findAllUserBookings(String username);

    @Modifying
    @Query(value =  "UPDATE booking " +
                    "SET reserved_tickets_count = ?1 " +
                    "WHERE id = ?2 ",
            nativeQuery = true)
    void updateByTickets(int reservedTicketsCount, long id);
}
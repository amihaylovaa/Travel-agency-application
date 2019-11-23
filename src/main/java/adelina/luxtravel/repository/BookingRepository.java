package adelina.luxtravel.repository;

import adelina.luxtravel.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(value = "SELECT * " +
                   "FROM booking" +
                   "WHERE id = ?1",
            nativeQuery = true)
    Booking getBooking(long id);

    @Modifying
    @Query(value = "DELETE " +
                   "FROM booking" +
                   "WHERE id = ?1",
            nativeQuery = true)
    void deleteBooking(long id);
}
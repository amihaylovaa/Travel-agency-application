package adelina.luxtravel.repository;

import adelina.luxtravel.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(value = "SELECT * " +
                   "FROM booking" +
                   "WHERE id = ?1",
            nativeQuery = true)
    Booking findById(long id);

    @Query(value = "SELECT * " +
                   "FROM booking" +
                   "WHERE user_id IN (" +
                   "SELECT id FROM user WHERE username = ?1",
            nativeQuery = true)
    List<Booking> findAllBookingsByUsername(String username);

    @Modifying
    @Query(value = "DELETE " +
                   "FROM booking" +
                   "WHERE id = ?1",
            nativeQuery = true)
    void deleteById(long id);
}
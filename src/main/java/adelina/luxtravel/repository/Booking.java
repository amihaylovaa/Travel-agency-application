package adelina.luxtravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Booking extends JpaRepository<Booking, Long> {
    @Query(value = "SELECT *" +
            "FROM booking+" +
            "WHERE userId=?1",
            nativeQuery = true)
    Booking getBookings(long userId);
}
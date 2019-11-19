package adelina.luxtravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<BookingRepository, Long> {
    @Query(value = "SELECT *" +
            "FROM booking+" +
            "WHERE userId=?1",
            nativeQuery = true)
    BookingRepository getBookings(long userId);
}
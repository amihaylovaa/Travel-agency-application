package adelina.luxtravel.repository;

import adelina.luxtravel.wrapper.BookingData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingDataRepository extends JpaRepository<BookingData, Long> {

}

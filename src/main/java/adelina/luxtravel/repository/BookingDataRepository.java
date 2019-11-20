package adelina.luxtravel.repository;

import adelina.luxtravel.domain.wrapper.BookingData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BookingDataRepository extends JpaRepository<BookingData, Long> {

    @Query(value = "CALL GetBookingData(?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    BookingData getBookingDataBySrcAndDst(String srcCity, String srcCountry, String srcContinent,
                                          String dstCity, String dstCountry, String dstContinent);

    @Query(value = "SELECT *" +
            "FROM  booking_data" +
            "WHERE from_date = 1? AND to_date = 2?", nativeQuery = true)
    BookingData getBookingByDates(LocalDate from, LocalDate to);

    @Modifying
    @Query(value = "CALL DeleteBookingData(?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    void DeleteBySourceAndDestination(String srcCity, String srcCountry, String srcContinent,
                                      String dstCity, String dstCountry, String dstContinent);
}
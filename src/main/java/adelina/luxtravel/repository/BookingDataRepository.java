package adelina.luxtravel.repository;

import adelina.luxtravel.domain.BookingData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BookingDataRepository extends JpaRepository<BookingData, Long> {
    @Query(value = "SELECT *" +
                   "FROM booking_data" +
                   "WHERE id = ?1",
            nativeQuery = true)
    BookingData getBookingDataById(long id);

    @Query(value = "SELECT *" +
                   "FROM booking_data" +
                   "WHERE from_date = ?1 AND to_date = ?2",
            nativeQuery = true)
    BookingData getBookingDataByDates(LocalDate from, LocalDate to);

    @Modifying
    @Query(value = "UPDATE booking_data" +
                   "SET source_id = ?1" +
                   "WHERE id = ?2",
            nativeQuery = true)
    BookingData updateSource(long newSrcId, long id);

    @Modifying
    @Query(value = "UPDATE booking_data" +
                   "SET destination_id = ?1" +
                   "WHERE id = ?2",
            nativeQuery = true)
    BookingData updateDestination(long newDstId, long id);

    @Modifying
    @Query(value = "UPDATE booking_data" +
                   "SET from_date = ?1 AND to_date = ?2 " +
                   "WHERE id = ?3",
            nativeQuery = true)
    void updateBookingDataByDates(LocalDate newFromDate, LocalDate newToDate, long id);

    @Modifying
    @Query(value = "UPDATE booking_data" +
                   "SET transport_id = ?1 " +
                   "WHERE id = ?2",
            nativeQuery = true)
    void updateTransport(long transportId, long id);

    @Modifying
    @Query(value = "DELETE *" +
                   "FROM booking_data" +
                   "WHERE id = ?1",
            nativeQuery = true)
    void deleteBookingData(long id);
}
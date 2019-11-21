package adelina.luxtravel.repository;

import adelina.luxtravel.domain.wrapper.BookingData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BookingDataRepository extends JpaRepository<BookingData, Long> {
    @Query(value = "SELECT *" +
                   "FROM booking_data" +
                   "WHERE source_id = ?1" +
                   "AND destination_id = ?2",
            nativeQuery = true)
    BookingData getBookingDataBySrcDstId(long srcId, long dstId);

    @Query(value = "SELECT *" +
                   "FROM  booking_data" +
                   "WHERE from_date = ?1 AND to_date = ?2",
            nativeQuery = true)
    BookingData getBookingDataByDates(LocalDate from, LocalDate to);

    @Modifying
    @Query(value = "UPDATE booking_data" +
                   "SET source_id = ?1" +
                   "WHERE source_id = ?2" +
                   "AND destination_id = ?3",
            nativeQuery = true)
    BookingData updateSource(long newSrcId, long prevSrcId, long dstId);

    @Modifying
    @Query(value = "UPDATE booking_data" +
                   "SET destination_id = ?1" +
                   "WHERE destination_id = ?2" +
                   "AND source_id =?3",
            nativeQuery = true)
    BookingData updateDestination(long newDstId, long prevDstId, long srcId);

    @Modifying
    @Query(value = "UPDATE booking_data" +
                   "SET from_date = ?1" +
                   "WHERE from_date = ?2" +
                   "AND to_date = ?3", nativeQuery = true)
    void updateBookingDateByFromDate(LocalDate newFromDate, LocalDate prevFromDate,
                                     LocalDate toDate);

    @Modifying
    @Query(value = "UPDATE booking_data" +
                   "SET to_date = ?1" +
                   "WHERE to_date = ?2" +
                   "AND from_date = ?3",
            nativeQuery = true)
    void updateBookingDataByDates(LocalDate newToDate, LocalDate prevToDate,
                                  LocalDate fromDate);

    @Modifying
    @Query(value = "UPDATE booking_data" +
                    "SET from_date = ?1 AND to_date = ?2 " +
                    "WHERE to_date = ?3" +
                    "AND from_date = ?4", nativeQuery = true)
    void updateBookingDataByDates(LocalDate newFromDate, LocalDate newToDate,
                                  LocalDate prevFromDate, LocalDate prevToDate);

    @Modifying
    @Query(value = "UPDATE booking_data" +
                   "SET transport_id = ?1 " +
                   "WHERE source_id = ?2" +
                   "AND destination_id = ?3", nativeQuery = true)
    void updateTransport(long transportId, long srcId, long dstId);

    @Modifying
    @Query(value = "DELETE *" +
                   "FROM booking_data" +
                   "WHERE source_id = ?1" +
                   "AND destination_id = ?2", nativeQuery = true)
    void deleteBySourceAndDestinationId(long srcId, long dstId);
}
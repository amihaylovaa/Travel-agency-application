package adelina.luxtravel.repository;

import adelina.luxtravel.domain.BookingData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingDataRepository extends JpaRepository<BookingData, Long> {
    @Query(value = "SELECT from_date, to_date, traveling_point.name," +
                   "traveling_point.name, transport.class" +
                   "FROM booking_data, traveling_point, transport" +
                   "WHERE id = ?1",
            nativeQuery = true)
    BookingData findBookingDataById(long id);

    @Query(value = "SELECT from_date, to_date, traveling_point.name" +
                   "traveling_point.name, transport.class" +
                   "FROM booking_data" +
                   "WHERE from_date = ?1 AND to_date = ?2",
            nativeQuery = true)
    List<BookingData> findBookingsDataByDates(LocalDate from, LocalDate to);

    @Query(value = "SELECT from_date, to_date, traveling_point.name" +
                   "traveling_point.name, transport.class" +
                   "FROM booking_data" +
                   "WHERE source_id = ?1",
            nativeQuery = true)
    BookingData findBookingDataBySourceId(long id);

    @Query(value = "SELECT from_date, to_date, traveling_point.name" +
                   "traveling_point.name, transport.class" +
                   "FROM booking_data" +
                   "WHERE destination_id = ?1",
            nativeQuery = true)
    BookingData findBookingDataByDestinationId(long id);

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
    @Query(value = "DELETE " +
                   "FROM booking_data" +
                   "WHERE id = ?1",
            nativeQuery = true)
    void deleteBookingDataById(long id);

    @Modifying
    @Query(value = "UPDATE booking " +
                   "SET count_available_tickets = count_available_tickets - ?1" +
                   "WHERE id = ?2",
            nativeQuery = true)
    void decrementCountAvailableTickets(int countTickets, long id);

    @Modifying
    @Query(value = "UPDATE booking " +
                   "SET count_available_tickets = count_available_tickets + ?1" +
                   "WHERE id = ?2",
            nativeQuery = true)
    void incrementCountAvailableTickets(int countTickets, long id);
}
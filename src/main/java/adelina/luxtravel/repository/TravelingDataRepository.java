package adelina.luxtravel.repository;

import adelina.luxtravel.domain.TravelingData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TravelingDataRepository extends JpaRepository<TravelingData, Long> {
    @Query(value = "SELECT from_date, to_date, traveling_point.name" +
                   "traveling_point.name, transport.class" +
                   "FROM booking_data, traveling_point" +
                   "WHERE from_date = ?1 AND to_date = ?2",
            nativeQuery = true)
    List<TravelingData> findByDates(LocalDate from, LocalDate to);

    @Modifying
    @Query(value = "UPDATE booking_data" +
                   "SET from_date = ?1 AND to_date = ?2 " +
                   "WHERE id = ?3",
            nativeQuery = true)
    void updateDates(LocalDate newFromDate, LocalDate newToDate, long id);

    @Modifying
    @Query(value = "UPDATE booking_data" +
                   "SET transport_id = ?1 " +
                   "WHERE id = ?2",
            nativeQuery = true)
    void updateTransport(long transportId, long id);

    @Modifying
    @Query(value = "UPDATE booking " +
                   "SET count_available_tickets = count_available_tickets - ?1" +
                   "WHERE id = ?2",
            nativeQuery = true)
    void reserveTickets(int reservedTicketsCount, long id);

    @Modifying
    @Query(value = "UPDATE booking " +
                   "SET count_available_tickets = count_available_tickets + ?1" +
                   "WHERE id = ?2",
            nativeQuery = true)
    void cancelTicketReservation(int reservedTicketsCount, long id);

    @Modifying
    @Query(value = "UPDATE booking " +
            "SET count_available_tickets = count_available_tickets + ?1" +
            "WHERE id = ?2",
            nativeQuery = true)
    void updateTicketsReservation(int reservedTicketsCount, long id);
}
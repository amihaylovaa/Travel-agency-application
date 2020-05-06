package adelina.luxtravel.repository;

import adelina.luxtravel.domain.TravelingData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents repository for traveling data
 */
@Repository
public interface TravelingDataRepository extends JpaRepository<TravelingData, Long> {
    /**
     * Searches for traveling data by dates
     *
     * @param from starting dates
     * @param to   ending date
     * @return the found traveling data
     */
    @Query(value = "SELECT * " +
            "FROM traveling_data " +
            "WHERE from_date = ?1 AND to_date = ?2 ",
            nativeQuery = true)
    List<TravelingData> findByDates(LocalDate from, LocalDate to);

    /**
     * Gets available tickets count
     * for a particular traveling data by id
     *
     * @param travelingDataId traveling data's id
     * @return available tickets count
     */
    @Query(value = "SELECT available_tickets_count " +
            "FROM traveling_data " +
            "WHERE id = ?1 ",
            nativeQuery = true)
    int findAvailableTicketsCount(long travelingDataId);

    /**
     * Updates starting date of a traveling data by id
     *
     * @param newFromDate the new starting date
     * @param id          traveling data's id
     */
    @Modifying
    @Query(value = "UPDATE traveling_data " +
            "SET from_date = ?1 " +
            "WHERE id = ?2",
            nativeQuery = true)
    void updateFromDate(LocalDate newFromDate, long id);

    /**
     * Updates ending date of a traveling data by id
     *
     * @param newToDate the new ending date
     * @param id        traveling data's id
     */
    @Modifying
    @Query(value = "UPDATE traveling_data " +
            "SET to_date = ?1 " +
            "WHERE id = ?2",
            nativeQuery = true)
    void updateToDate(LocalDate newToDate, long id);

    /**
     * Reserves tickets for a particular traveling data by id
     *
     * @param reservedTicketsCount reserved tickets' count
     * @param id                   traveling data's id for which the tickets are reserved
     */
    @Modifying
    @Query(value = "UPDATE traveling_data " +
            "SET available_tickets_count = (available_tickets_count - ?1) " +
            "WHERE id = ?2",
            nativeQuery = true)
    void reserveTickets(int reservedTicketsCount, long id);

    /**
     * Cancels already reserved tickets
     * for a particular traveling data
     *
     * @param reservedTicketsCount the number of canceled tickets
     * @param id                   traveling data's id
     */
    @Modifying
    @Query(value = "UPDATE traveling_data " +
            "SET available_tickets_count = (available_tickets_count + ?1) " +
            "WHERE id = ?2",
            nativeQuery = true)
    void cancelTicketReservation(int reservedTicketsCount, long id);

    /**
     * Updates the count of already reserved ticket
     * for a particular traveling data by id
     *
     * @param reservedTicketsCount the new number of reserved tickets
     * @param id                   traveling data's id
     */
    @Modifying
    @Query(value = "UPDATE traveling_data " +
            "SET available_tickets_count = (available_tickets_count + ?1) " +
            "WHERE id = ?2",
            nativeQuery = true)
    void updateTicketsReservation(int reservedTicketsCount, long id);
}
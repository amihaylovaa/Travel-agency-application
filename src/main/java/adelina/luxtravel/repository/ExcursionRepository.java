package adelina.luxtravel.repository;

import adelina.luxtravel.domain.Excursion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents excursion's repository
 */
@Repository
public interface ExcursionRepository extends JpaRepository<Excursion, Long> {
    /**
     * Retrieves excursions by dates
     *
     * @param from starting date
     * @param to   ending date
     * @return list of excursions on these dates
     */
    @Query(value = "SELECT * " +
            "FROM excursions " +
            "WHERE from_date = ?1 AND to_date = ?2 ",
            nativeQuery = true)
    List<Excursion> findByDates(LocalDate from, LocalDate to);

    /**
     * Retrieves the count of available tickets for a particular excursion
     *
     * @param id excursion's id
     * @return the count of available tickets
     */
    @Query(value = "SELECT available_tickets_count " +
            "FROM excursions " +
            "WHERE id = ?1 ",
            nativeQuery = true)
    int findExcursionAvailableTicketsCountById(long id);

    /**
     * Updates the starting date of an excursion
     *
     * @param newFromDate the new starting date
     * @param id          excursion's id
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = "UPDATE excursions " +
            "SET from_date = ?1 " +
            "WHERE id = ?2",
            nativeQuery = true)
    void updateFromDate(LocalDate newFromDate, long id);

    /**
     * Updates the ending date of an excursion by id
     *
     * @param newToDate the new ending date
     * @param id        excursion's id
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = "UPDATE excursions " +
            "SET to_date = ?1 " +
            "WHERE id = ?2",
            nativeQuery = true)
    void updateToDate(LocalDate newToDate, long id);

    /**
     * Updates tickets count when a booking for particular excursion is canceled
     *
     * @param reservedTicketsCount the number of canceled tickets
     * @param id                   excursion's id
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = "UPDATE excursions " +
            "SET available_tickets_count = (available_tickets_count + ?1) " +
            "WHERE id = ?2",
            nativeQuery = true)
    void cancelTicketReservation(int reservedTicketsCount, long id);

    /**
     * Updates tickets count when a booking for particular excursion is made
     *
     * @param reservedTicketsCount the new number of reserved tickets
     * @param id                   excursion's id
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = "UPDATE excursions " +
            "SET available_tickets_count = (available_tickets_count - ?1) " +
            "WHERE id = ?2",
            nativeQuery = true)
    void reserveTickets(int reservedTicketsCount, long id);
}
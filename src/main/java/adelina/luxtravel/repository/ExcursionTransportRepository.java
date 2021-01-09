package adelina.luxtravel.repository;

import adelina.luxtravel.domain.wrapper.ExcursionTransport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Represents excursion-transport's repository
 */
@Repository
public interface ExcursionTransportRepository extends JpaRepository<ExcursionTransport, Long> {

    /**
     * Retrieves excursion's id by transport and price
     *
     * @param transportId transport's id
     * @param price       excursion's price
     * @return the searched excursion's id
     */
    @Query(value = "SELECT excursion_id " +
            "FROM excursions_transport " +
            "WHERE transport_id=?1 " +
            "AND price=?2 ",
            nativeQuery = true)
    long getExcursionId(long transportId, double price);
}

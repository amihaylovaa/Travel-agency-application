package adelina.luxtravel.repository;

import adelina.luxtravel.domain.transport.Transport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Represents transport's repository
 */
@Repository
public interface TransportRepository extends JpaRepository<Transport, Long> {
    /**
     * Retrieves specific transports by class
     *
     * @param transportClass transport's class
     * @param type           transport's type (bus or airplane)
     * @return list of transports
     */
    @Query(value = "SELECT id, type, class " +
            "FROM transports " +
            "WHERE class = ?1 " +
            "AND type= ?2",
            nativeQuery = true)
    List<Transport> findSpecificTransportsByTransportClass(String transportClass, String type);

    /**
     * Retrieves all buses
     *
     * @return list of buses
     */
    @Query(value = "SELECT id, class, type " +
            "FROM transports " +
            "WHERE type='bus'",
            nativeQuery = true)
    List<Transport> findAllBuses();

    /**
     * Retrieves all airplanes
     *
     * @return list of airplanes
     */
    @Query(value = "SELECT id, class, type " +
            "FROM transports " +
            "WHERE type='airplane'",
            nativeQuery = true)
    List<Transport> findAllAirplanes();

    /**
     * Updates transport's class
     *
     * @param transportClass the new transport class
     * @param id             transport's id
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = "UPDATE transports " +
            "SET class = ?1 " +
            "WHERE id = ?2 ",
            nativeQuery = true)
    void updateTransportClass(String transportClass, long id);
}
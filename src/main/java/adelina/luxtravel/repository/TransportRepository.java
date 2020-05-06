package adelina.luxtravel.repository;

import adelina.luxtravel.domain.transport.Transport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Represents repository for transport
 */
@Repository
public interface TransportRepository extends JpaRepository<Transport, Long> {
    /**
     * Searches for all buses by class
     *
     * @param transportClass the transport class
     * @return list of all buses of that class
     */
    @Query(value = "SELECT t.id, t.transport_class, t.transport_type, b.id as clazz_ " +
            "FROM transport t, bus b " +
            "WHERE t.transport_class = ?1 " +
            "AND t.id = b.id ",
            nativeQuery = true)
    List<Transport> findAllBusesByClass(String transportClass);

    /**
     * Searches for all airplanes by class
     *
     * @param transportClass the transport class
     * @return list of all airplanes of that class
     */
    @Query(value = "SELECT t.id, t.transport_class, t.transport_type, a.id as clazz_ " +
            "FROM transport t , airplane a " +
            "WHERE transport_class = ?1 " +
            "AND a.id = t.id",
            nativeQuery = true)
    List<Transport> findAllAirplanesByClass(String transportClass);

    /**
     * Searches for all buses
     *
     * @return list of all buses
     */
    @Query(value = "SELECT t.id, t.transport_class, t.transport_type, b.id as clazz_ " +
            "FROM transport t , bus b " +
            "WHERE t.id = b.id",
            nativeQuery = true)
    List<Transport> findAllBuses();

    /**
     * Searches for all airplanes
     *
     * @return list of all airplanes
     */
    @Query(value = "SELECT t.id, t.transport_class, t.transport_type, a.id as clazz_ " +
            "FROM transport t , airplane a " +
            "WHERE t.id = a.id",
            nativeQuery = true)
    List<Transport> findAllAirplanes();

    /**
     * Updates transport's class by id
     *
     * @param transportClass the new transport class
     * @param id             transport's id
     */
    @Modifying
    @Query(value = "UPDATE transport " +
            "SET transport_class = ?1 " +
            "WHERE id = ?2 ",
            nativeQuery = true)
    void updateClass(String transportClass, long id);
}
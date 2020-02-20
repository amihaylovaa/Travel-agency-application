package adelina.luxtravel.repository;

import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.domain.transport.TransportClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportRepository extends JpaRepository<Transport, Long> {
    @Query(value = "SELECT t.id, t.transport_class, b.id as clazz_ " +
                   "FROM transport t, bus b " +
                   "WHERE t.transport_class = ?1 " +
                   "AND b.id = t.id " ,
            nativeQuery = true)
    List<Transport> findAllBusesByClass(String transportClass);

    @Query(value = "SELECT t.id, t.transport_class, a.id as clazz_ " +
                   "FROM transport t , airplane a " +
                   "WHERE transport_class = ?1 " +
                   "AND t.id = a.id",
            nativeQuery = true)
    List<Transport> findAllAirplanesByClass(String transportClass);

    @Query(value = "SELECT t.id, t.transport_class, b.id as clazz_ " +
                   "FROM transport t , bus b " +
                   "WHERE t.id = b.id",
            nativeQuery = true)
    List<Transport> findAllBuses();

    @Query(value = "SELECT t.id, t.transport_class, a.id as clazz_ " +
                   "FROM transport t , airplane a " +
                   "WHERE t.id = a.id",
            nativeQuery = true)
    List<Transport> findAllAirplanes();

    @Modifying
    @Query(value = "UPDATE transport " +
                   "SET transport_class = ?1 " +
                   "WHERE id = ?2 ",
            nativeQuery = true)
    void updateClass(String transportClass, long id);
}
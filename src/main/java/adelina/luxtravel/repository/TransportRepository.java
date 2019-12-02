package adelina.luxtravel.repository;

import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.domain.transport.TransportClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportRepository extends JpaRepository<Transport, Long> {
    @Query(value = "SELECT class" +
                   "FROM transport" +
                   "WHERE class = ?1 AND id IN" +
                   "(SELECT id FROM bus WHERE id=transport.id)",
            nativeQuery = true)
    List<Transport> getAllBusesByClass(TransportClass vehicleClass);

    @Query(value = "SELECT class" +
                   "FROM transport" +
                   "WHERE class = ?1 AND id IN" +
                   "(SELECT id FROM airplane WHERE id=transport.id)",
            nativeQuery = true)
    List<Transport> getAllAirplanesByClass(TransportClass vehicleClass);

    @Query(value = "SELECT class" +
                   "FROM transport" +
                   "id IN" +
                   "(SELECT id FROM bus WHERE id=transport.id)",
            nativeQuery = true)
    List<Transport> getAllBuses();

    @Query(value = "SELECT class" +
                   "FROM transport" +
                   "id IN" +
                   "(SELECT id FROM airplane WHERE id=transport.id)",
            nativeQuery = true)
    List<Transport> getAllAirplanes();
}
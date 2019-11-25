package adelina.luxtravel.repository;

import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.domain.transport.TransportClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportRepository extends JpaRepository<Transport, Long> {
    @Query(value = "SELECT *" +
                   "FROM transport" +
                   "WHERE class = ?1 AND id IN" +
                   "(SELECT id FROM bus WHERE id=transport.id)",
              nativeQuery = true)
    Transport getAllBusesByClass(TransportClass vehicleClass);

    @Query(value = "SELECT *" +
                   "FROM transport" +
                   "WHERE class = ?1 AND id IN" +
                   "(SELECT id FROM airplane WHERE id=transport.id)",
             nativeQuery = true)
    Transport getAllAirplanesByClass(TransportClass vehicleClass);

    @Query(value = "SELECT *" +
                   "FROM bus" +
                   "WHERE id IN" +
                   "(SELECT id FROM transport WHERE id=bus.id)",
             nativeQuery = true)
    Transport getAllBuses();

    @Query(value = "SELECT *" +
                   "FROM airplane" +
                   "WHERE id IN" +
                   "(SELECT id FROM transport WHERE id=airplane.id)",
            nativeQuery = true)
    Transport getAllAirplanes();
}
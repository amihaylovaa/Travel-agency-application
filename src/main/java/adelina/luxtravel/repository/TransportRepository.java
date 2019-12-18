package adelina.luxtravel.repository;

import adelina.luxtravel.domain.transport.Airplane;
import adelina.luxtravel.domain.transport.Bus;
import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.domain.transport.TransportClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportRepository extends JpaRepository<Transport, Long> {
    @Query(value = "BEGIN;" +

                   "INSERT INTO transport(class)" +
                   "VALUES (?1);" +

                   "INSERT INTO bus" +
                   "SELECT id" +
                   "FROM transport" +
                   "WHERE class = ?1;" +

                    "COMMIT;",
            nativeQuery = true)
    Bus saveBus(TransportClass transportClass);

    @Query(value = "BEGIN;" +

                   "INSERT INTO transport(class)" +
                   "VALUES (?1);" +

                   "INSERT INTO airplane" +
                   "SELECT id" +
                   "FROM transport" +
                   "WHERE class = ?1;" +

                  "COMMIT;",
            nativeQuery = true)
    Airplane saveAirplane(TransportClass transportClass);

    @Query(value = "SELECT class" +
                   "FROM transport" +
                   "WHERE class = ?1 AND id IN" +
                   "(SELECT id FROM bus WHERE id=transport.id)",
            nativeQuery = true)
    List<Transport> findAllBusesByClass(TransportClass transportClass);

    @Query(value = "SELECT class" +
                   "FROM transport" +
                   "WHERE class = ?1 AND id IN" +
                   "(SELECT id FROM airplane WHERE id=transport.id)",
            nativeQuery = true)
    List<Transport> findAllAirplanesByClass(TransportClass transportClass);

    @Query(value = "SELECT class" +
                   "FROM transport" +
                   "id IN" +
                   "(SELECT id FROM bus WHERE id=transport.id)",
            nativeQuery = true)
    List<Transport> findAllBuses();

    @Query(value = "SELECT class" +
                   "FROM transport" +
                   "id IN" +
                   "(SELECT id FROM airplane WHERE id=transport.id)",
            nativeQuery = true)
    List<Transport> findAllAirplanes();

    @Modifying
    @Query(value = "UPDATE transport" +
                   "SET class = ?1" +
                   "WHERE id = ?2",
            nativeQuery = true)
    void updateClass(TransportClass transportClass, long id);
}
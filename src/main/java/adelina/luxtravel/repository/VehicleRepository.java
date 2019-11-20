package adelina.luxtravel.repository;

import adelina.luxtravel.domain.transport.Vehicle;
import adelina.luxtravel.domain.transport.VehicleClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    @Query(value = "SELECT *" +
            "FROM vehicle" +
            "WHERE class = ?1 id IN" +
            "(SELECT id FROM bus WHERE id=vehicle.id)"
            , nativeQuery = true)
    Vehicle getAllBusesByClass(VehicleClass vehicleClass);

    @Query(value = "SELECT *" +
            "FROM vehicle" +
            "WHERE class = ?1 id IN" +
            "(SELECT id FROM airplane WHERE id=vehicle.id)"
            , nativeQuery = true)
    Vehicle getAllAirplanesByClass(VehicleClass vehicleClass);

    @Query(value = "SELECT *" +
            "FROM vehicle" +
            "WHERE id IN" +
            "(SELECT id FROM bus WHERE id=vehicle.id)",
            nativeQuery = true)
    Vehicle getAllBuses();

    @Query(value = "SELECT *" +
            "FROM vehicle" +
            "WHERE id IN" +
            "(SELECT id FROM airplane WHERE id=vehicle.id)",
            nativeQuery = true)
    Vehicle getAllAirplanes();
}
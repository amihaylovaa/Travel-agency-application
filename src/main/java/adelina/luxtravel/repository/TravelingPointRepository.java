package adelina.luxtravel.repository;

import adelina.luxtravel.domain.TravelingPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TravelingPointRepository extends JpaRepository<TravelingPoint, Long> {
    @Query(value = "SELECT latitude, longitude, name " +
                   "FROM traveling_point" +
                   "WHERE name = ?1",
            nativeQuery = true)
    TravelingPoint findTravelingPoint(String name);
}
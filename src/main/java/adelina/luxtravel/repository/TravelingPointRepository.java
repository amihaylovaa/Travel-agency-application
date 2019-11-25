package adelina.luxtravel.repository;

import adelina.luxtravel.domain.TravelingPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TravelingPointRepository extends JpaRepository<TravelingPoint, Long> {
    @Query(value = "SELECT * " +
                   "FROM traveling_point" +
                   "WHERE id = ?1",
            nativeQuery = true)
    TravelingPoint getTravelingPointById(long id);

    @Query(value = "SELECT * " +
                   "FROM traveling_point" +
                   "WHERE name = ?1",
            nativeQuery = true)
    TravelingPoint getTravelingPointByName(String name);
}

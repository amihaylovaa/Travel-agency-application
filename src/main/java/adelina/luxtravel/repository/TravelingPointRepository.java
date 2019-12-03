package adelina.luxtravel.repository;

import adelina.luxtravel.domain.TravelingPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TravelingPointRepository extends JpaRepository<TravelingPoint, Long> {
    @Query(value = "SELECT latitude, longitude, name " +
                   "FROM traveling_point" +
                   "WHERE name = ?1",
            nativeQuery = true)
    TravelingPoint findByName(String name);

    @Modifying
    @Query(value = "DELETE " +
                   "FROM traveling_point" +
                   "WHERE name = ?1",
            nativeQuery = true)
    void deleteByName(String name);

    @Modifying
    @Query(value = "UPDATE traveling_point" +
                   "SET latitude = ?1" +
                   "AND longitude = ?2  " +
                   "AND name = ?3" +
                   "WHERE id = ?4",
            nativeQuery = true)
      void update(double latitude, double longitude, String newName, String currentName);
}
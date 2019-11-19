package adelina.luxtravel.repository;

import adelina.luxtravel.wrapper.TravelPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelPointRepository extends JpaRepository<TravelPoint, Long> {
    @Query("SELECT *" +
            "FROM travel_point" +
            "WHERE city_id IN " +
            "(SELECT id FROM city" +
            "WHERE name = ?1 AND country_id IN " +
            "(SELECT id FROM country" +
            "where name =?2 and continent_id IN" +
            "(SELECT id FROM continent" +
            "WHERE name=?3) " +
            ")" +
            ")")
    TravelPoint getTravelPoint(String city, String country, String continent);
}
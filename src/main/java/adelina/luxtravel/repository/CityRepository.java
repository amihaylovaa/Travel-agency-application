package adelina.luxtravel.repository;

import adelina.luxtravel.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    @Query(value = "SELECT * FROM city WHERE name = ?1 " +
            "AND country_id IN (SELECT id FROM country WHERE name = ?2 ))", nativeQuery = true)
    City getCityByName(String cityName, String countryName);
}

package adelina.luxtravel.repository;

import adelina.luxtravel.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    @Query(value = "SELECT * " +
                   "FROM city " +
                   "WHERE id = ?1 ",
            nativeQuery = true)
    City getCityById(long id);

    @Query(value = "SELECT * " +
                   "FROM city " +
                   "WHERE name = ?1 ",
            nativeQuery = true)
    City getCityByName(String name);
}
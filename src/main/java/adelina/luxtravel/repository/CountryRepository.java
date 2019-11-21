package adelina.luxtravel.repository;

import adelina.luxtravel.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    @Query(value = "SELECT *" +
                   "FROM country" +
                   "WHERE id = ?1 ",
            nativeQuery = true)
    Country getCountryById(long id);

    @Query(value = "SELECT *" +
                   "FROM country" +
                   "WHERE name = ?1 ",
            nativeQuery = true)
    Country getCountryByName(String name);
}
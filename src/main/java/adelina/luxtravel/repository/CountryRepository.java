package adelina.luxtravel.repository;

import adelina.luxtravel.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    @Query("SELECT * FROM country WHERE name = 1? ")
    Country getContinentByName(String countryName);
}

package adelina.luxtravel.repository;

import adelina.luxtravel.domain.Continent;
import adelina.luxtravel.domain.ContinentList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContinentRepository extends JpaRepository<Continent, Long> {

    @Query("SELECT * FROM continent WHERE name = ?1 ")
    Continent getContinentByName(ContinentList continent);
}

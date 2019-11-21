package adelina.luxtravel.repository;

import adelina.luxtravel.domain.Continent;
import adelina.luxtravel.domain.ContinentList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContinentRepository extends JpaRepository<Continent, Long> {
    @Query(value = "SELECT * " +
                   "FROM continent " +
                   "WHERE id = ?1 ",
            nativeQuery = true)
    Continent getContinentById(long id);

    @Query(value = "SELECT *" +
                   "FROM continent " +
                   "WHERE name = ?1 ",
            nativeQuery = true)
    Continent getContinentByName(String name);
}
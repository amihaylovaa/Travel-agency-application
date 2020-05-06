package adelina.luxtravel.repository;

import adelina.luxtravel.domain.TravelingPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Represents repository for traveling point
 */
public interface TravelingPointRepository extends JpaRepository<TravelingPoint, Long> {
    /**
     * Searches for traveling point by name
     *
     * @param name traveling point's name
     * @return the found traveling point
     */
    @Query(value = "SELECT * " +
            "FROM traveling_point " +
            "WHERE name = ?1",
            nativeQuery = true)
    Optional<TravelingPoint> findByName(String name);

    /**
     * Updates traveling point's current name
     *
     * @param newName     the new name
     * @param currentName the current name
     */
    @Modifying
    @Query(value = "UPDATE traveling_point " +
            "SET name = ?1 " +
            "WHERE name = ?2 ",
            nativeQuery = true)
    void updateName(String newName, String currentName);
}
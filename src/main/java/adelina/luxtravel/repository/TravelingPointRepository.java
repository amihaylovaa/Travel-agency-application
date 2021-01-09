package adelina.luxtravel.repository;

import adelina.luxtravel.domain.TravelingPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Represents traveling point's repository
 */
public interface TravelingPointRepository extends JpaRepository<TravelingPoint, Long> {

    /**
     * Retrieves traveling points containing specific name
     *
     * @param name traveling point's name
     * @return the searched traveling point
     */
    List<TravelingPoint> findByNameContaining(String name);
}
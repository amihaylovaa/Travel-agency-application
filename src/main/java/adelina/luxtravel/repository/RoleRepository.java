package adelina.luxtravel.repository;

import adelina.luxtravel.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Represents role's repository
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Retrieves role's id by its name
     *
     * @param name role's name
     * @return the searched id
     */
    @Query(value = "SELECT id " +
            "FROM roles " +
            "WHERE role=?1 "
            , nativeQuery = true)
    long getIdFromName(String name);
}

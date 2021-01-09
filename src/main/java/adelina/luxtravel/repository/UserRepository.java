package adelina.luxtravel.repository;

import adelina.luxtravel.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Represents user's repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves user by username
     *
     * @param username user's username
     * @return the searched user
     */
    Optional<User> findByUsername(String username);

    /**
     * Retrieves user by email
     *
     * @param email user's email
     * @return the searched user
     */
    Optional<User> findByEmail(String email);

    /**
     * Deletes user by id
     *
     * @param id user's id
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = "DELETE " +
            "FROM users " +
            "WHERE id = ?1 ",
            nativeQuery = true)
    void deleteById(long id);

    /**
     * Updates user's password
     *
     * @param newPassword the new password
     * @param username    user's username
     * @return 1 for successfully updated password
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = "UPDATE users " +
            "SET password = ?1 " +
            "WHERE username = ?2 ",
            nativeQuery = true)
    int updatePassword(String newPassword, String username);

    /**
     * Updates user's email
     *
     * @param newEmail the new email
     * @param username user's username
     */
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE users " +
            "SET email = ?1 " +
            "WHERE username = ?2",
            nativeQuery = true)
    void updateEmail(String newEmail, String username);

    /**
     * Updates user's role
     *
     * @param roleId   the new assigned role
     * @param username user's username
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = "UPDATE users " +
            "SET role_id = ?1 " +
            "WHERE username = ?2 ",
            nativeQuery = true)
    void updateRole(long roleId, String username);
}
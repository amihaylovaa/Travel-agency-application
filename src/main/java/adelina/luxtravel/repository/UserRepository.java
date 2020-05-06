package adelina.luxtravel.repository;

import adelina.luxtravel.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Represents repository for user
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Under construction
     */
    @Query(value = "SELECT * " +
            "FROM user " +
            "WHERE email = ?1 " +
            "AND password = ?2",
            nativeQuery = true)
    Optional<User> login(String email, String password);

    /**
     * Searches for a user by username
     *
     * @param username user's username
     * @return the found user
     */
    @Query(value = "SELECT * " +
            "FROM  user " +
            "WHERE username = ?1",
            nativeQuery = true)
    Optional<User> findByUsername(String username);

    /**
     * Searches for a user by email
     *
     * @param email user's email
     * @return the found user
     */
    @Query(value = "SELECT * " +
            "FROM  user " +
            "WHERE email = ?1",
            nativeQuery = true)
    Optional<User> findByEmail(String email);

    /**
     * Updates user's password by username
     *
     * @param newPassword the new password
     * @param username    user's username
     */
    @Modifying
    @Query(value = "UPDATE user " +
            "SET password = ?1 " +
            "WHERE username = ?2 ",
            nativeQuery = true)
    void updatePassword(String newPassword, String username);

    /**
     * Updates user's email by current email
     *
     * @param newEmail     the new email
     * @param currentEmail the current email
     */
    @Modifying
    @Query(value = "UPDATE user " +
            "SET email = ?1 " +
            "WHERE email = ?2",
            nativeQuery = true)
    void updateEmail(String newEmail, String currentEmail);

    /**
     * Deletes user by username
     *
     * @param username user's username
     */
    @Modifying
    @Query(value = "DELETE " +
            "FROM user " +
            "WHERE username = ?1 ",
            nativeQuery = true)
    void deleteByUsername(String username);

    /**
     * Deletes user by email
     *
     * @param email user's email
     */
    @Modifying
    @Query(value = "DELETE " +
            "FROM user " +
            "WHERE email = ?1 ",
            nativeQuery = true)
    void deleteByEmail(String email);
}
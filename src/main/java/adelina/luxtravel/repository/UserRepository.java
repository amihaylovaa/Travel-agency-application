package adelina.luxtravel.repository;

import adelina.luxtravel.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT email, username" +
                   "FROM  user" +
                   "WHERE username = ?1",
            nativeQuery = true)
    User findByUsername(String username);

    @Query(value = "SELECT email, username" +
                   "FROM  user" +
                   "WHERE email = ?1",
            nativeQuery = true)
    User findByEmail(String email);

    @Modifying
    @Query(value = "UPDATE user " +
                   "SET password = ?1" +
                   "WHERE username = ?2 ",
            nativeQuery = true)
    void updatePassword(String newPassword, String username);

    @Modifying
    @Query(value = "UPDATE user " +
                   "SET email = ?1" +
                   "WHERE email = ?2",
            nativeQuery = true)
    void updateEmail(String newEmail, String currentEmail);

    @Modifying
    @Query(value = "DELETE " +
                   "FROM user " +
                   "WHERE username = ?1 ",
            nativeQuery = true)
    void deleteByUsername(String username);

    @Modifying
    @Query(value = "DELETE " +
                   "FROM user " +
                   "WHERE email = ?1 ",
            nativeQuery = true)
    void deleteByEmail(String email);
}
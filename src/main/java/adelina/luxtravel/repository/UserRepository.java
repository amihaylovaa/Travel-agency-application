package adelina.luxtravel.repository;

import adelina.luxtravel.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT *" +
                   "FROM  user" +
                   "WHERE username = ?1",
            nativeQuery = true)
    User getUserByUsername(String username);

    @Modifying
    @Query(value = "UPDATE user " +
                   "SET password = ?1" +
                   "WHERE username = ?2",
            nativeQuery = true)
    void updatePasswordByUsername(String newPassword, String userName);

    @Modifying
    @Query(value = "UPDATE user " +
                   "SET email = ?1" +
                   "WHERE username = ?3",
            nativeQuery = true)
    void updateEmailByUsername(String newEmail, String userName);

    @Modifying
    @Query(value = "DELETE " +
                   "FROM user " +
                   "WHERE username = ?1",
            nativeQuery = true)
    void deleteUserByUsername(String username);
}
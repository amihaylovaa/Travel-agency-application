package adelina.luxtravel.repository;

import adelina.luxtravel.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM  user WHERE username = ?1", nativeQuery = true)
    User getUserByUsername(String username);

    @Modifying
    @Query(value = "DELETE FROM user WHERE username = ?1", nativeQuery = true)
    void deleteUserByUserName(String username);

    @Modifying
    @Query(value = "UPDATE user SET password = ?1 WHERE currentPassword = ?2 and username = ?3", nativeQuery = true)
    void updateUserPassword(String currentPassword, String newPassword, String userName);
}

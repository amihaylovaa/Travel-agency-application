package adelina.luxtravel.repository;

import adelina.luxtravel.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT * FROM  user WHERE username = ?1")
    User getUserByUsername(String username);

    @Query("DELETE FROM user WHERE username = ?1")
    void deleteUserByUserName(String username);

    @Modifying
    @Query("UPDATE user SET password = ?1 WHERE currentPassword = ?2 and username = ?3")
    void updateUserPassword(String currentPassword, String newPassword, String userName);
}

package adelina.luxtravel.domain;

import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;
    @Column(name = "username", unique = true, nullable = false, length = 32)
    private String username;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @OneToMany(mappedBy = "user",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    List<Booking> bookings;

    public User(String email, String password, String username) {
        initializeFields(username, email, password);
    }

    public User(User user) {
        id = user.id;
        username = user.username;
        email = user.email;
        password = user.password;
    }

    private void initializeFields(String username, String email, String password) {
        if (username == null || username.isEmpty()) {
            throw new FailedInitializationException("Invalid username");
        } else if (email == null || email.isEmpty()) {
            throw new FailedInitializationException("Invalid email");
        } else if (password == null || password.isEmpty() || password.length() < 8) {
            throw new FailedInitializationException("Invalid password");
        }
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
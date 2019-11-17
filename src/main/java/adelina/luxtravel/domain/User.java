package adelina.luxtravel.domain;

import adelina.luxtravel.exception.FailedInitializationException;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "username", unique = true, nullable = false, length = 32)
    private String username;

    public User(String email, String password, String username) {
        initializeFields(username, email, password);
    }

    private void initializeFields(String username, String email, String password) {
        if (username == null || username.isEmpty()) {
            throw new FailedInitializationException("Invalid username");
        } else if (email == null || email.isEmpty()) {
            throw new FailedInitializationException("Invalid email");
        } else if (password == null || password.isEmpty() || password.length() < 8) {
            throw new FailedInitializationException("Invalid password");
        }
        this.username = new String(username);
        this.email = new String(email);
        this.password = new String(password);
    }
}
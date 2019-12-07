package adelina.luxtravel.domain;

import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "username", unique = true, nullable = false, length = 32)
    private String username;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @OneToMany(mappedBy = "user",
              cascade = CascadeType.ALL,
              orphanRemoval = true
              )
    List<Booking> bookings;

    public User(String email, String password, String username) {
        initializeFields(username, email, password);
    }

    public User(long id, String email, String password, String username) {
        this(email, password, username);
        this.id = id;
    }

    public User(User user) {
        this(user.id, user.email, user.password, user.username);
    }

    private void initializeFields(String username, String email, String password) {
        if (StringUtils.isEmpty(username)) {
            throw new FailedInitializationException("Invalid username");
        } else if (StringUtils.isEmpty(password)) {
            throw new FailedInitializationException("Invalid email");
        } else if (StringUtils.isEmpty(password) || password.length() < 8) {
            throw new FailedInitializationException("Invalid password");
        } else {
            this.username = username;
            this.email = email;
            this.password = password;
        }
    }
}
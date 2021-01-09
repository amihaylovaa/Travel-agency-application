package adelina.luxtravel.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

/**
 * Represents a user
 */
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @NotEmpty(message = "Username can not be null or empty")
    @Length(min = 5, message = "Username can not be less than 5 characters")
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @NotEmpty(message = "Email can not be null or empty")
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Setter
    @NotEmpty(message = "Password can not be null or empty")
    @Length(min = 5, message = "Password can not be less than 5 characters")
    @Column(name = "password", nullable = false)
    private String password;
    @Setter
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public User(long id, String username, String email, String password, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(long id, String email) {
        this.id = id;
        this.email = email;
    }

    public User(String username, Role role) {
        this.username = username;
        this.role = role;
    }
}
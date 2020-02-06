package adelina.luxtravel.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @NotEmpty(message = "Username can not be null or empty")
    @Length(min = 4, max = 32, message = "Username can not be less than 4 or more than 32 characters")
    @Column(name = "username", unique = true, nullable = false, length = 32)
    private String username;
    @NotEmpty(message = "Email can not be null or empty")
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Setter
    @NotEmpty(message = "Password can not be null or empty")
    @Length(min = 8, message = "Password can not be less than 8 characters")
    @Column(name = "password", nullable = false)
    private String password;
    @NotNull(message = "List of bookings can not be null")
    @OneToMany(mappedBy = "user",
               cascade = CascadeType.REMOVE,
               orphanRemoval = true
             )
    private List<Booking> bookings;

    public User(User user) {
        this(user.id, user.username, user.email, user.password);
    }

    public User(long id, String username, String email, String password) {
        this(username, email, password);
        this.id = id;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.bookings = new ArrayList<>();
    }
}
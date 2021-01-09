package adelina.luxtravel.domain;

import adelina.luxtravel.enumeration.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Represents the role assigned to a participant in the system
 */
@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private RoleType roleType;

    public Role(RoleType roleType) {
        this.roleType = roleType;
    }
}
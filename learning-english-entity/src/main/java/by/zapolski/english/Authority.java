package by.zapolski.english;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name = "authorities")
@Data
@NoArgsConstructor
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true
    )
    private String value;

    @Transient
    @ManyToMany(
            fetch = FetchType.LAZY,
            mappedBy = "authorities"
    )
    private Set<User> users;

    @Override
    public String getAuthority() {
        return value;
    }
}

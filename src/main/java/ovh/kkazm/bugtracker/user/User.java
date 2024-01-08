package ovh.kkazm.bugtracker.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "users")

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    private String roles;

    public List<SimpleGrantedAuthority> getRoles() {
        return Arrays.stream(roles
                        .split(",")
                )
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
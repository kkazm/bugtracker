package ovh.kkazm.bugtracker.user.dtos;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * DTO for {@link ovh.kkazm.bugtracker.user.User}
 */
public record UserDto(Long id,
                      String username,
                      List<SimpleGrantedAuthority> roles,
                      Instant createdAt) implements Serializable {
}

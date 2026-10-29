package ovh.kkazm.bugtracker.user.dtos;

import ovh.kkazm.bugtracker.user.User;

import java.time.Instant;

/**
 * Projection for {@link User}
 */
public interface UserInfo {
    Long getId();

    String getUsername();

    Instant getCreatedAt();
}

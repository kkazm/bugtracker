package ovh.kkazm.bugtracker.issue;

import java.time.ZonedDateTime;

/**
 * Projection for {@link Issue}
 */
public interface IssueInfo {
    Long getId();

    String getTitle();

    String getDescription();

    ZonedDateTime getCreatedAt();

    ZonedDateTime getUpdatedAt();

    UserInfo getReporter();

    /**
     * Projection for {@link ovh.kkazm.bugtracker.user.User}
     */
    interface UserInfo {
        Long getId();

        String getUsername();
    }
}
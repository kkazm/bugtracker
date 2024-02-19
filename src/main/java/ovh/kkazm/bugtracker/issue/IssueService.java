package ovh.kkazm.bugtracker.issue;

import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class IssueService {

    /**
     * Projection for {@link Issue}
     */
    public static interface IssueInfo {
        Long getId();
        String getTitle();
        ZonedDateTime getCreatedAt();
        String getDescription();
    }
}

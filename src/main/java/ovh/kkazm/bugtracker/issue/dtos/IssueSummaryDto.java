package ovh.kkazm.bugtracker.issue.dtos;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link ovh.kkazm.bugtracker.issue.Issue}
 */
public record IssueSummaryDto(Long id,
                              String title,
                              Instant createdAt,
                              String description) implements Serializable {
}

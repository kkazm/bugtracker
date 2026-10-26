package ovh.kkazm.bugtracker.issue.dtos;

import ovh.kkazm.bugtracker.issue.Issue;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link Issue}
 */
public record IssueCreatedDto(Long id,
                              String title,
                              Long projectId,
                              String description,
                              Instant createdAt) implements Serializable {
}

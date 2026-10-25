package ovh.kkazm.bugtracker.issue.dtos;

import ovh.kkazm.bugtracker.issue.Issue;

import java.io.Serializable;

/**
 * DTO for {@link Issue}
 */
public record IssueCreatedDto(Long id,
                              String title,
                              Long projectId) implements Serializable {
}

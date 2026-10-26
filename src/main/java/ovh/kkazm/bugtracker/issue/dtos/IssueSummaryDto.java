package ovh.kkazm.bugtracker.issue.dtos;

import java.io.Serializable;

/**
 * DTO for {@link ovh.kkazm.bugtracker.issue.Issue}
 */
public record IssueSummaryDto(Long id,
                              String title,
                              String createdAt) implements Serializable {
}

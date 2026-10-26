package ovh.kkazm.bugtracker.issue.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ovh.kkazm.bugtracker.issue.Issue;

import java.io.Serializable;

/**
 * DTO for {@link Issue}
 */
public record CreateIssueDto(@NotBlank String title,
                             @NotNull @Positive Long projectId,
                             @NotBlank String description) implements Serializable {
}

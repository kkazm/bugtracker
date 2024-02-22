package ovh.kkazm.bugtracker.issue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

/**
 * DTO for {@link Issue}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record IssueDto(
        @NotNull @NotBlank
        String title,
        String description,
        @NotNull @Positive
        Long projectId,
        String assigneeUsername
) implements Serializable {
}
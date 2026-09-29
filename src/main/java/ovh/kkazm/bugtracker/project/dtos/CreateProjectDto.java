package ovh.kkazm.bugtracker.project.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateProjectDto(
        @NotBlank
        @NotEmpty
        @Size(min = 3, max = 255)
        String projectName
) {
}

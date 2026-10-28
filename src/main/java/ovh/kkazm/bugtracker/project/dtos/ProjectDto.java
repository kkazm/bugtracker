package ovh.kkazm.bugtracker.project.dtos;

import java.io.Serializable;

/**
 * DTO for {@link ovh.kkazm.bugtracker.project.Project}
 */
public record ProjectDto(Long id,
                         String name) implements Serializable {
}

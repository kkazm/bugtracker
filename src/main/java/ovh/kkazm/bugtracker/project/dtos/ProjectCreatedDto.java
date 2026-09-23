package ovh.kkazm.bugtracker.project.dtos;

import ovh.kkazm.bugtracker.project.Project;

import java.io.Serializable;

/**
 * DTO for {@link Project}
 */
public record ProjectCreatedDto(
        Long id,
        String name
) implements Serializable {
}

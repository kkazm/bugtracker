package ovh.kkazm.bugtracker.project.dtos;

import ovh.kkazm.bugtracker.user.dtos.UserDto;

import java.io.Serializable;

/**
 * DTO for {@link ovh.kkazm.bugtracker.project.Project}
 */
public record ProjectDto(Long id,
                         String name,
                         UserDto owner) implements Serializable {
}

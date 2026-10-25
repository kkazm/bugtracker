package ovh.kkazm.bugtracker.project.dtos;

import ovh.kkazm.bugtracker.issue.dtos.IssueSummaryDto;
import ovh.kkazm.bugtracker.project.Project;
import ovh.kkazm.bugtracker.user.dtos.UserDto;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link Project}
 */
public record ProjectDetailDto(Long id,
                               String name,
                               UserDto owner,
                               Set<IssueSummaryDto> issues) implements Serializable {
}

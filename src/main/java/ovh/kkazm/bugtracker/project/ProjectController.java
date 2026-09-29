package ovh.kkazm.bugtracker.project;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ovh.kkazm.bugtracker.project.dtos.CreateProjectDto;
import ovh.kkazm.bugtracker.project.dtos.ProjectCreatedDto;

import java.net.URI;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
class ProjectController {

    private final ProjectService projectService;

    /**
     * Create a {@linkplain  Project} and save it to the database.
     *
     * @param projectDto Description of the {@link Project} to create.
     * @return A description of the created {@link Project} as {@link ovh.kkazm.bugtracker.project.dtos.ProjectCreatedDto}.
     */
    @PostMapping
    public ResponseEntity<ProjectCreatedDto>
    createProject(@Valid @RequestBody CreateProjectDto projectDto) {
        ProjectCreatedDto createdProjectDto = projectService.create(projectDto);
        return ResponseEntity.created(URI.create("/projects/" +
                        createdProjectDto.id()))
                .body(createdProjectDto);
    }

}

package ovh.kkazm.bugtracker.project;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
     * @param projectDTO Description of the {@link Project} to create.
     * @return A description of the created {@link Project} as {@link ovh.kkazm.bugtracker.project.dtos.ProjectCreatedDto}.
     */
    @PostMapping
    public ResponseEntity<ProjectCreatedDto> createProject(@RequestBody CreateProjectDto projectDto) {
        ProjectCreatedDto createdProject = projectService.createProject(projectDto);
        return ResponseEntity.created(URI.create("/projects/")) // TODO
                .body(createdProject);
    }

}

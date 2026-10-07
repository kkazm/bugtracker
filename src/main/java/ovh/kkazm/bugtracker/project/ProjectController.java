package ovh.kkazm.bugtracker.project;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ovh.kkazm.bugtracker.project.dtos.CreateProjectDto;
import ovh.kkazm.bugtracker.project.dtos.ProjectCreatedDto;
import ovh.kkazm.bugtracker.project.dtos.ProjectDto;

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

    // FIXME Somebody can sort 'IgnoringCase'
    /**
     * Get a page of Projects.
     * @param pageable A Pageable describing pagination
     * @return A Page of Projects
     */
    @GetMapping
    @PageableAsQueryParam // TODO Check how this looks
    public ResponseEntity<PagedModel<ProjectCreatedDto>>
    getAllProjects(@PageableDefault(sort = "name") Pageable pageable) {
        if (pageable.getSort().stream().anyMatch(order -> !order.getProperty().equals("name"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Projects can only be sorted by name");
        }
        return ResponseEntity.ok(projectService.getAll(pageable));
    }

    /**
     * Get a single Project searching by ID.
     *
     * @param id The ID of the Project to find.
     * @return A Project
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long id) {
        ProjectDto projectDto = projectService.get(id);
        return ResponseEntity.ok(projectDto);
    }

}

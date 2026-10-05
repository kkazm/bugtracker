package ovh.kkazm.bugtracker.project;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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

    /**
     * FIXME Somebody can sort 'IgnoringCase'
     */
    @GetMapping
    public ResponseEntity<PagedModel<ProjectCreatedDto>>
    getAllProjects(@PageableDefault(sort = "name") Pageable pageable) {
        if (pageable.getSort().stream().anyMatch(order -> !order.getProperty().equals("name"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Projects can only be sorted by name");
        }
        return ResponseEntity.ok(projectService.getAll(pageable));
    }

}

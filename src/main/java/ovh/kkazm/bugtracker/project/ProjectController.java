package ovh.kkazm.bugtracker.project;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ovh.kkazm.bugtracker.project.ProjectService.CreateProjectRequest;

import java.util.List;

@RestController
@CrossOrigin("*") // TODO CORS
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/projects")
    public void
    createProject(@RequestBody @Valid CreateProjectRequest createProjectRequest, Authentication authentication) {
        projectService.createProject(createProjectRequest, authentication);
    }

    @GetMapping("/projects")
    public Page<ProjectService.ProjectInfo>
    getAllPublicProjects(@RequestParam Integer page, @RequestParam Integer per_page, @RequestParam String direction) {
        Pageable pageable = PageRequest.of(page, per_page);
        return projectService.getAllPublicProjects(pageable);
    }

}

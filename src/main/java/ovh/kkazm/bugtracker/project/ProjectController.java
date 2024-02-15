package ovh.kkazm.bugtracker.project;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ovh.kkazm.bugtracker.project.ProjectService.CreateProjectRequest;
import ovh.kkazm.bugtracker.project.ProjectService.ProjectDto;
import ovh.kkazm.bugtracker.project.ProjectService.ProjectInfo;

@RestController
@CrossOrigin("*") // TODO CORS
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectMapper projectMapper;

    @PostMapping("/projects")
    public ResponseEntity<ProjectDto>
    createProject(
            @Valid @RequestBody CreateProjectRequest createProjectRequest,
            Authentication authentication,
            BindingResult bindingResult
    ) {
//        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().build();
        Project project = projectService.createProject(createProjectRequest, authentication);
        ProjectDto projectDto = projectMapper.toDto(project);
        return ResponseEntity.ok(projectDto);
    }

    @GetMapping("/projects")
    public Page<ProjectInfo>
    getAllPublicProjects(@RequestParam Integer page, @RequestParam Integer per_page, @RequestParam String direction) {
        Pageable pageable = PageRequest.of(page, per_page);
        return projectService.getAllPublicProjects(pageable);
    }

}

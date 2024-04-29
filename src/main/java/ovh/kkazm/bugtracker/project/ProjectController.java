package ovh.kkazm.bugtracker.project;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ovh.kkazm.bugtracker.issue.IssueService.IssueInfo;
import ovh.kkazm.bugtracker.project.ProjectService.ProjectCreationRequest;
import ovh.kkazm.bugtracker.project.ProjectService.ProjectDto;
import ovh.kkazm.bugtracker.project.ProjectService.ProjectInfo;

@RestController
@CrossOrigin("*") // TODO CORS
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/projects")
    public ResponseEntity<ProjectDto>
    createProject(@Valid @RequestBody ProjectCreationRequest projectCreationRequest,
                  BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().build();
        ProjectDto projectDto = projectService.createProject(projectCreationRequest);
        return ResponseEntity.ok(projectDto);
    }

    @GetMapping("/projects")
    public ResponseEntity<Page<ProjectInfo>>
    getAllPublicProjects(Pageable pageable) {
        Pageable page = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Direction.DESC, "createdAt")); // TODO Refactor
        Page<ProjectInfo> allPublicProjects = projectService.getAllPublicProjects(page);
        return ResponseEntity.ok(allPublicProjects);
    }

    @GetMapping("/projects/{projectId}/issues")
    public ResponseEntity<Page<IssueInfo>>
    getAllProjectIssues(@PathVariable Long projectId, Pageable pageable) {
        Pageable page = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Direction.DESC, "createdAt")); // TODO Refactor
        Page<IssueInfo> allProjectIssues = projectService.getAllProjectIssues(projectId, page);
        return ResponseEntity.ok(allProjectIssues);
    }

    @PatchMapping("/projects")
    public ResponseEntity<Page<IssueInfo>>
    patchProject(@Valid @RequestBody ProjectDto projectDto) {
        throw new NotImplementedException();
    }

}

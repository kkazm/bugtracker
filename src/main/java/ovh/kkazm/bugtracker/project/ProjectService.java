package ovh.kkazm.bugtracker.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ovh.kkazm.bugtracker.issue.IssueInfo;
import ovh.kkazm.bugtracker.issue.IssueRepository;
import ovh.kkazm.bugtracker.user.User;
import ovh.kkazm.bugtracker.user.UserRepository;
import ovh.kkazm.bugtracker.user.UserService;
import ovh.kkazm.bugtracker.user.UserService.UserInfo;

import java.io.Serializable;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final IssueRepository issueRepository;

    @Transactional
    public Project createProject(CreateProjectRequest createProjectRequest, Authentication authentication) {
        final var name = createProjectRequest.projectName();
        final var project = new Project();
        project.setName(name);
        User user = userRepository.findByUsername(authentication.getName()) // TODO getReferenceById?
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid project owner"));
        project.setOwner(user);
        if (projectRepository.existsByNameIgnoreCase(project.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A project with this name already exists.");
        } else {
            return projectRepository.save(project);
        }
    }

    @Transactional
    public Page<ProjectInfo> getAllPublicProjects(Pageable pageable) {
        return projectRepository.findBy(pageable);
    }

    @Transactional
    public Page<IssueInfo> getAllProjectIssues(Long projectId, Pageable page) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project with this ID, does not exist");
        }
        return issueRepository.getAllProjectIssues(projectId, page);
    }

    public record CreateProjectRequest(
            @NotNull
            @NotBlank
            String projectName
    ) {
    }

    /**
     * Projection for {@link Project}
     */
    public interface ProjectInfo {
        Long getId();
        String getName();
        UserInfo getOwner();
    }

    /**
     * DTO for {@link Project}
     */
    @JsonIgnoreProperties(ignoreUnknown = true) // TODO Ignore or no?
    public record ProjectDto(Long id, String name) implements Serializable {
    }

}

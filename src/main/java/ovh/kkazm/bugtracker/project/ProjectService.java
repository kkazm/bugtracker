package ovh.kkazm.bugtracker.project;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ovh.kkazm.bugtracker.user.User;
import ovh.kkazm.bugtracker.user.UserRepository;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createProject(CreateProjectRequest createProjectRequest, Authentication authentication) {
        // TODO Use MapStruct
        final var name = createProjectRequest.projectName();
        final var project = new Project();
        project.setName(name);
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid project owner"));
        project.setOwner(user);
        projectRepository.save(project);
    }

    @Transactional
    public Page<ProjectInfo> getAllPublicProjects(Pageable pageable) {
        return projectRepository.findBy(pageable);
    }

    public record CreateProjectRequest(String projectName) {
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
     * Projection for {@link User}
     */
    public static interface UserInfo {
        Long getId();

        String getUsername();

        String getRoles();
    }
}

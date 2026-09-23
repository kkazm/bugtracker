package ovh.kkazm.bugtracker.project;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.kkazm.bugtracker.project.dtos.CreateProjectDto;
import ovh.kkazm.bugtracker.project.dtos.ProjectCreatedDto;
import ovh.kkazm.bugtracker.user.User;
import ovh.kkazm.bugtracker.user.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectMapper projectMapper;

    @PersistenceContext
    private final EntityManager em;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional
    public ProjectCreatedDto createProject(CreateProjectDto projectDTO) {
        Project project = new Project();
        project.setName(projectDTO.projectName());
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        User reporter = userRepository.findByUsername(username) // FIXME
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found: " + username));
        project.setReporter(reporter);
        Project savedProject = projectRepository.save(project);
        return projectMapper.toDto(savedProject);
    }

}

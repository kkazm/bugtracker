package ovh.kkazm.bugtracker.project;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ovh.kkazm.bugtracker.exceptions.ResourceNotFoundException;
import ovh.kkazm.bugtracker.issue.IssueRepository;
import ovh.kkazm.bugtracker.project.dtos.CreateProjectDto;
import ovh.kkazm.bugtracker.project.dtos.ProjectCreatedDto;
import ovh.kkazm.bugtracker.project.dtos.ProjectDetailDto;
import ovh.kkazm.bugtracker.project.dtos.ProjectDto;
import ovh.kkazm.bugtracker.project.events.ProjectCreatedEvent;
import ovh.kkazm.bugtracker.projectmembership.ProjectMembership;
import ovh.kkazm.bugtracker.projectmembership.ProjectMembershipRepository;
import ovh.kkazm.bugtracker.projectmembership.ProjectRole;
import ovh.kkazm.bugtracker.user.User;
import ovh.kkazm.bugtracker.user.UserRepository;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectService implements ApplicationEventPublisherAware {

    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final IssueRepository issueRepository;
    private final ProjectMembershipRepository projectMembershipRepository;
    private ApplicationEventPublisher publisher;

    @Transactional
    public ProjectCreatedDto create(CreateProjectDto projectDTO) {
        if (projectRepository.existsByName(projectDTO.projectName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Project with this name already exists"); // FIXME Change exception type
        }
        String authenticatedUsername = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName(); // FIXME
        User user = userRepository.findByUsername(authenticatedUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user", authenticatedUsername));

        var project = new Project();
        var pm = new ProjectMembership();
        project.setName(projectDTO.projectName());
        pm.setUser(user);
        pm.setProject(project);
        pm.setRole(ProjectRole.OWNER);
        project.setProjectMemberships(Set.of(pm));

        Project savedProject = projectRepository.save(project);
        ProjectMembership savedPm = projectMembershipRepository.save(pm);

        ProjectCreatedDto projectCreatedDto = projectMapper.toDto(savedProject);
        publisher.publishEvent(new ProjectCreatedEvent(this, projectCreatedDto));
        return projectCreatedDto;
    }

    public PagedModel<ProjectDetailDto> getAPageOfProjects(Pageable pageable) {
        Page<ProjectDetailDto> page = projectRepository.findDetailedAllBy(pageable)
                .map(projectMapper::toDetailDto);
        return new PagedModel<>(page);
    }

    @Transactional(readOnly = true)
    public ProjectDto get(Long id) {
        Project p = projectRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Project", id));
        return projectMapper.toProjectDto(p);
    }

    @Transactional
    public void delete(Long id) {
        projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", id));
        issueRepository.deleteByProjectId(id);
        projectRepository.deleteProjectById(id);
    }

    @Override
    public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher applicationEventPublisher) {
        publisher = applicationEventPublisher;
    }

}

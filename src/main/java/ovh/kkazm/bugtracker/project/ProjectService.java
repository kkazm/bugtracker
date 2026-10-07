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
import ovh.kkazm.bugtracker.project.dtos.CreateProjectDto;
import ovh.kkazm.bugtracker.project.dtos.ProjectCreatedDto;
import ovh.kkazm.bugtracker.project.dtos.ProjectDto;
import ovh.kkazm.bugtracker.project.events.ProjectCreatedEvent;
import ovh.kkazm.bugtracker.user.User;
import ovh.kkazm.bugtracker.user.UserRepository;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectService implements ApplicationEventPublisherAware {

    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private ApplicationEventPublisher publisher;

    @Transactional
    public ProjectCreatedDto create(CreateProjectDto projectDTO) {
        if (projectRepository.existsByName(projectDTO.projectName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Project with this name already exists");
        }
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName(); // FIXME
        User owner = userRepository.findByUsername(username) // FIXME
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found: " + username));

        Project project = new Project();
        project.setName(projectDTO.projectName());
        project.setOwner(owner);
        Project savedProject = projectRepository.save(project);

        ProjectCreatedDto projectCreatedDto = projectMapper.toDto(savedProject);
        publisher.publishEvent(new ProjectCreatedEvent(this, projectCreatedDto));
        return projectCreatedDto;
    }

    @Transactional(readOnly = true)
    public PagedModel<ProjectCreatedDto> getAll(Pageable pageable) {
        Page<ProjectCreatedDto> page = projectRepository.findAll(pageable)
                .map(projectMapper::toDto);
        return new PagedModel<>(page);
    }

    @Transactional(readOnly = true)
    public ProjectDto get(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        Project p = project.orElseThrow();
        return projectMapper.toDto1(p);
    }

    @Override
    public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher applicationEventPublisher) {
        publisher = applicationEventPublisher;
    }

}

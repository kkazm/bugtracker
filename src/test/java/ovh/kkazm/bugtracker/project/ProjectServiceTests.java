package ovh.kkazm.bugtracker.project;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import ovh.kkazm.bugtracker.project.dtos.CreateProjectDto;
import ovh.kkazm.bugtracker.project.dtos.ProjectCreatedDto;
import ovh.kkazm.bugtracker.project.events.ProjectCreatedEvent;
import ovh.kkazm.bugtracker.user.User;
import ovh.kkazm.bugtracker.user.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTests {

    @Mock
    private ProjectMapper projectMapper;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectService.setApplicationEventPublisher(publisher);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("testUser", "password")
        );
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createSavesProjectForAuthenticatedOwnerAndPublishesCreatedEvent() {
        CreateProjectDto request = new CreateProjectDto("TestProject");
        User owner = new User();
        owner.setId(7L);
        owner.setUsername("testUser");
        Project savedProject = new Project();
        savedProject.setId(11L);
        savedProject.setName("TestProject");
        savedProject.setOwner(owner);
        ProjectCreatedDto expected = new ProjectCreatedDto(11L, "TestProject");

        when(projectRepository.existsByName("TestProject")).thenReturn(false);
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(owner));
        when(projectRepository.save(any(Project.class))).thenReturn(savedProject);
        when(projectMapper.toDto(savedProject)).thenReturn(expected);

        ProjectCreatedDto actual = projectService.create(request);

        assertSame(expected, actual);
        ArgumentCaptor<Project> projectCaptor = ArgumentCaptor.forClass(Project.class);
        verify(projectRepository).save(projectCaptor.capture());
        assertEquals("TestProject", projectCaptor.getValue().getName());
        assertSame(owner, projectCaptor.getValue().getOwner());
        verify(projectMapper).toDto(savedProject);

        ArgumentCaptor<ProjectCreatedEvent> eventCaptor = ArgumentCaptor.forClass(ProjectCreatedEvent.class); // TODO
        verify(publisher).publishEvent(eventCaptor.capture());
        assertSame(projectService, eventCaptor.getValue().getSource());
    }

    @Test
    void createThrowsConflictWhenProjectNameAlreadyExists() {
        when(projectRepository.existsByName("TestProject")).thenReturn(true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> projectService.create(new CreateProjectDto("TestProject"))
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        verify(projectRepository, never()).save(any(Project.class));
        verifyNoInteractions(userRepository, projectMapper, publisher);
    }

    @Test
    void createFailsWhenAuthenticatedUserCannotBeFound() {
        when(projectRepository.existsByName("TestProject")).thenReturn(false);
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> projectService.create(new CreateProjectDto("TestProject"))
        );

        assertEquals("Authenticated user not found: testUser", exception.getMessage());
        verify(projectRepository, never()).save(any(Project.class));
        verifyNoInteractions(projectMapper, publisher);
    }
}

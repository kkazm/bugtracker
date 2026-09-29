package ovh.kkazm.bugtracker.project;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import ovh.kkazm.bugtracker.project.dtos.CreateProjectDto;
import ovh.kkazm.bugtracker.user.User;
import ovh.kkazm.bugtracker.user.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTests {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectService projectService;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createProjectSavesAuthenticatedUserAsReporter() {
        User reporter = User.builder()
                .id(17L)
                .username("alice")
                .roles("USER")
                .password("encoded-password")
                .build();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("alice", "token")
        );
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(reporter));

        projectService.createProject(new CreateProjectDto("API improvements"));

        ArgumentCaptor<Project> projectCaptor = ArgumentCaptor.forClass(Project.class);
        verify(projectRepository).save(projectCaptor.capture());
        assertEquals("API improvements", projectCaptor.getValue().getName());
        assertSame(reporter, projectCaptor.getValue().getOwner());
        verify(userRepository).findByUsername("alice");
    }

    @Test
    void createProjectFailsIfAuthenticatedUserDoesNotExist() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("missing-user", "token")
        );
        when(userRepository.findByUsername("missing-user")).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> projectService.createProject(new CreateProjectDto("API improvements")));

        verify(projectRepository, never()).save(org.mockito.ArgumentMatchers.any(Project.class));
    }
}

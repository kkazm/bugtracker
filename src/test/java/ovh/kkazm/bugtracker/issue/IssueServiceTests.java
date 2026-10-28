package ovh.kkazm.bugtracker.issue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ovh.kkazm.bugtracker.exceptions.ResourceNotFoundException;
import ovh.kkazm.bugtracker.issue.dtos.CreateIssueDto;
import ovh.kkazm.bugtracker.issue.dtos.IssueCreatedDto;
import ovh.kkazm.bugtracker.project.Project;
import ovh.kkazm.bugtracker.project.ProjectRepository;
import ovh.kkazm.bugtracker.user.User;
import ovh.kkazm.bugtracker.user.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IssueServiceTests {

    private static final String ISSUE_DESCRIPTION = "Submitting valid credentials shows an error.";

    @Mock
    private IssueMapper issueMapper;

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private IssueService issueService;

    @Test
    void createIssueSavesIssueWithProjectAndAuthenticatedReporter() {
        CreateIssueDto request = new CreateIssueDto("Login fails", 17L, ISSUE_DESCRIPTION);
        Project project = new Project();
        project.setId(17L);
        User reporter = new User();
        reporter.setId(23L);
        reporter.setUsername("testUser");
        Issue mappedIssue = new Issue();
        IssueCreatedDto expected = new IssueCreatedDto(
                31L,
                "Login fails",
                17L,
                ISSUE_DESCRIPTION
        );

        when(projectRepository.findById(17L)).thenReturn(Optional.of(project));
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(reporter));
        when(issueMapper.toEntity(request)).thenReturn(mappedIssue);
        when(issueRepository.save(any(Issue.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(issueMapper.toIssueCreatedDto(mappedIssue)).thenReturn(expected);

        IssueCreatedDto actual = issueService.createIssue(request, "testUser");

        assertSame(expected, actual);
        ArgumentCaptor<Issue> issueCaptor = ArgumentCaptor.forClass(Issue.class);
        verify(issueRepository).save(issueCaptor.capture());
        assertSame(project, issueCaptor.getValue().getProject());
        assertSame(reporter, issueCaptor.getValue().getReporter());
        verify(userRepository).findByUsername("testUser");
    }

    @Test
    void createIssueFailsWhenAuthenticatedUserCannotBeFound() {
        CreateIssueDto request = new CreateIssueDto("Login fails", 17L, ISSUE_DESCRIPTION);
        when(projectRepository.findById(17L)).thenReturn(Optional.of(new Project()));
        when(userRepository.findByUsername("missingUser")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> issueService.createIssue(request, "missingUser")
        );

        assertEquals("User: ", exception.getResource());
        assertEquals("missingUser", exception.getId());
        verify(issueRepository, never()).save(any(Issue.class));
        verifyNoInteractions(issueMapper);
    }

    @Test
    void createIssueFailsWhenProjectDoesNotExist() {
        CreateIssueDto request = new CreateIssueDto("Login fails", 17L, ISSUE_DESCRIPTION);
        when(projectRepository.findById(17L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> issueService.createIssue(request, "testUser")
        );

        assertEquals("Project", exception.getResource());
        assertEquals(17L, exception.getId());
        verifyNoInteractions(userRepository, issueMapper, issueRepository);
    }
}

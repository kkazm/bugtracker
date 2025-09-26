package ovh.kkazm.bugtracker.issue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ovh.kkazm.bugtracker.project.ProjectRepository;
import ovh.kkazm.bugtracker.user.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class IssueServiceTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private IssueRepository issueRepository;
    //    private final IssueMapper issueMapper;
    @InjectMocks
    private IssueService issueService;

    @Test
    void createIssue() {
        MockitoAnnotations.openMocks(this);
        IssueService.IssueDto issueDto = new IssueService.IssueDto("Title", "description", 1L, "konrad");

        var token = new UsernamePasswordAuthenticationToken("Hello", "");

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        try (MockedStatic<SecurityContextHolder> mocked = mockStatic(SecurityContextHolder.class)) {
            SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
            mocked.when(SecurityContextHolder::getContext).thenReturn(emptyContext);
            assertEquals(emptyContext, SecurityContextHolder.getContext());
            mocked.verify(SecurityContextHolder::getContext);
        }
        assertNull(SecurityContextHolder.getContext().getAuthentication());

//        issueService.createIssue(issueDto, token);
    }

}
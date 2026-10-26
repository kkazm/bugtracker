package ovh.kkazm.bugtracker.issue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ovh.kkazm.bugtracker.issue.dtos.CreateIssueDto;
import ovh.kkazm.bugtracker.issue.dtos.IssueCreatedDto;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IssueControllerTests {

    @Mock
    private IssueService issueService;

    @InjectMocks
    private IssueController issueController;

    @Test
    void createIssueReturnsCreatedResponseAndUsesRequestPrincipalAsReporter() {
        CreateIssueDto request = new CreateIssueDto("Login fails", 17L);
        IssueCreatedDto createdIssue = new IssueCreatedDto(31L, "Login fails", 17L);
        Principal principal = () -> "testUser";
        when(issueService.createIssue(request, "testUser")).thenReturn(createdIssue);

        var response = issueController.createIssue(request, principal);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("/issues/31", response.getHeaders().getLocation().toString());
        assertSame(createdIssue, response.getBody());
        verify(issueService).createIssue(request, "testUser");
    }
}

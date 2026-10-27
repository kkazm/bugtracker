package ovh.kkazm.bugtracker.issue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ovh.kkazm.bugtracker.issue.dtos.CreateIssueDto;
import ovh.kkazm.bugtracker.issue.dtos.IssueCreatedDto;
import ovh.kkazm.bugtracker.issue.dtos.IssueSummaryDto;

import java.security.Principal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IssueControllerTests {

    private static final String ISSUE_DESCRIPTION = "Submitting valid credentials shows an error.";

    @Mock
    private IssueService issueService;

    @InjectMocks
    private IssueController issueController;

    @Test
    void createIssueReturnsCreatedResponseAndUsesRequestPrincipalAsReporter() {
        CreateIssueDto request = new CreateIssueDto("Login fails", 17L, ISSUE_DESCRIPTION);
        IssueCreatedDto createdIssue = new IssueCreatedDto(
                31L,
                "Login fails",
                17L,
                ISSUE_DESCRIPTION
        );
        Principal principal = () -> "testUser";
        when(issueService.createIssue(request, "testUser")).thenReturn(createdIssue);

        var response = issueController.createIssue(request, principal);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("/issues/31", response.getHeaders().getLocation().toString());
        assertSame(createdIssue, response.getBody());
        verify(issueService).createIssue(request, "testUser");
    }

    @Test
    void getIssuesByIdReturnsIssueFromService() {
        IssueSummaryDto issue = new IssueSummaryDto(
                31L,
                "Login fails",
                Instant.parse("2026-01-15T10:15:30Z"),
                ISSUE_DESCRIPTION
        );
        when(issueService.get(31L)).thenReturn(issue);

        var response = issueController.getIssuesById(31L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(issue, response.getBody());
        verify(issueService).get(31L);
    }

    @Test
    void deleteProjectByIdDeletesIssueAndReturnsNoContent() {
        var response = issueController.deleteProjectById(31L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(issueService).delete(31L);
    }
}

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
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
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
                ISSUE_DESCRIPTION,
                Instant.parse("2026-01-15T10:15:30Z")
        );
        Principal principal = () -> "testUser";
        when(issueService.createIssue(request, "testUser")).thenReturn(createdIssue);

        var response = issueController.createIssue(request, principal);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("/issues/31", response.getHeaders().getLocation().toString());
        assertSame(createdIssue, response.getBody());
        verify(issueService).createIssue(request, "testUser");
    }
}

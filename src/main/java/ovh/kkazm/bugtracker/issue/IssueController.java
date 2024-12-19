package ovh.kkazm.bugtracker.issue;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ovh.kkazm.bugtracker.issue.IssueService.IssueInfo;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    @PostMapping("/issues")
    public ResponseEntity<IssueService.CreateIssueDto>
    createIssue(@Valid @RequestBody IssueService.CreateIssueDto createIssueDto, Authentication authentication) {
        IssueService.CreateIssueDto issue = issueService.createIssue(createIssueDto, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(issue);
    }

    @GetMapping("/issues/{issueId}")
    public ResponseEntity<IssueInfo>
    getIssue(@PathVariable Long issueId) {
        try {
            return ResponseEntity.ok(issueService.getIssue(issueId));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
        }
    }

}

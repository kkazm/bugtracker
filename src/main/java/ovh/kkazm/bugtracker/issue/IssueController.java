package ovh.kkazm.bugtracker.issue;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ovh.kkazm.bugtracker.issue.IssueService.IssueDto;
import ovh.kkazm.bugtracker.issue.IssueRepository.IssueInfo;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    @PostMapping("/issues")
    public ResponseEntity<IssueDto>
    createIssue(@Valid @RequestBody IssueDto issueDto) {
        IssueDto issue;
        try {
            issue = issueService.createIssue(issueDto);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
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

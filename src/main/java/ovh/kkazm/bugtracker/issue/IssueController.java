package ovh.kkazm.bugtracker.issue;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IssueController {

    public final IssueService issueService;

    @PostMapping("/issues")
    public ResponseEntity<IssueDto>
    createIssue(@Valid @RequestBody IssueDto issueDto, Authentication authentication) {
        IssueDto issue = issueService.createIssue(issueDto, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(issue);
    }

}

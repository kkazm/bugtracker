package ovh.kkazm.bugtracker.issue;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ovh.kkazm.bugtracker.issue.dtos.CreateIssueDto;
import ovh.kkazm.bugtracker.issue.dtos.IssueCreatedDto;

import java.net.URI;

/**
 * Endpoints related to the Issues
 */
@RestController
@RequestMapping("/issues")
@RequiredArgsConstructor
class IssueController {

    private final IssueService issueService;

    /**
     * Create an Issue
     *
     * @param createIssueDto Object describing the Issue.
     * @return The created Issue.
     */
    @PostMapping
    ResponseEntity<IssueCreatedDto>
    createIssue(@Valid @RequestBody CreateIssueDto createIssueDto) {
        IssueCreatedDto result = issueService.createIssue(createIssueDto);
        URI locationUri = URI.create("/issues/" + result.id().toString());
        return ResponseEntity.created(locationUri).body(result);
    }

}

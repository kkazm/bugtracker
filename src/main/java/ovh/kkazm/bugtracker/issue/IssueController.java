package ovh.kkazm.bugtracker.issue;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ovh.kkazm.bugtracker.issue.dtos.CreateIssueDto;
import ovh.kkazm.bugtracker.issue.dtos.IssueCreatedDto;
import ovh.kkazm.bugtracker.issue.dtos.IssueSummaryDto;

import java.net.URI;
import java.security.Principal;

/**
 * Endpoints related to the Issues
 */
@RestController
@RequiredArgsConstructor
class IssueController {

    private final IssueService issueService;

    /**
     * Create an Issue
     *
     * @param createIssueDto Object describing the Issue.
     * @return The created Issue
     */
    @PostMapping("/issues")
    public ResponseEntity<IssueCreatedDto>
    createIssue(@Valid @RequestBody CreateIssueDto createIssueDto, Principal principal) {
        IssueCreatedDto result = issueService.createIssue(createIssueDto, principal.getName());
        URI locationUri = URI.create("/issues/" + result.id().toString());
        return ResponseEntity.created(locationUri).body(result);
    }

    /**
     * Get a page of Issues belonging to a Project.
     *
     * @param projectId The ID of the Project the Issues belong to
     * @param pageable A Pageable describing pagination
     * @return A Page of Issues
     */
    @GetMapping("/projects/{projectId}/issues")
    public ResponseEntity<PagedModel<IssueSummaryDto>>
    getAPageOfIssues(@PathVariable Long projectId,
                     @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
//        if (pageable.getSort().stream().anyMatch(order -> !order.getProperty().equals("title"))) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Issues can only be sorted by title");
//        }
        return ResponseEntity.ok(issueService.getAPageOfIssues(projectId, pageable));
    }

    /**
     * Get a single Issue searching by ID.
     *
     * @param id The ID of the Issue to find.
     * @return An Issue
     */
    @GetMapping("/issues/{id}")
    public ResponseEntity<IssueSummaryDto> getIssuesById(@PathVariable Long id) {
        IssueSummaryDto issueSummaryDto = issueService.get(id);
        return ResponseEntity.ok(issueSummaryDto);
    }

    /**
     * Delete an Issue by ID.
     *
     * @param id The ID of the Issue to delete
     * @return HTTP 204 on successful delete
     */
    @DeleteMapping("/issues/{id}")
    public ResponseEntity<Object> deleteProjectById(@PathVariable Long id) {
        issueService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

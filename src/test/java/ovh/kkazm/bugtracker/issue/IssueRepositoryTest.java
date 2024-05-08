package ovh.kkazm.bugtracker.issue;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ovh.kkazm.bugtracker.CommonDatabaseSetup;
import ovh.kkazm.bugtracker.issue.IssueService.IssueInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IssueRepositoryTest extends CommonDatabaseSetup {

    @Test
    void getAllProjectIssues() {
        Pageable page = PageRequest.of(0, 20, Sort.by(Sort.Direction.ASC, "createdAt"));
        Page<IssueInfo> allProjectIssues = issueRepository.getAllProjectIssues(1L, page);
        IssueInfo issueInfo = allProjectIssues.getContent().get(0);
        IssueInfo issueInfo2 = allProjectIssues.getContent().get(1);

        assertEquals(2, allProjectIssues.getTotalElements());
        assertEquals(TEST_ISSUE_TITLE, issueInfo.getTitle());
        assertEquals(TEST_ISSUE_TITLE_2, issueInfo2.getTitle());
        assertEquals(TEST_PROJECT_NAME, issueInfo.getProject().getName());
        assertEquals(TEST_PROJECT_NAME, issueInfo2.getProject().getName());
        assertEquals(TEST_USERNAME, issueInfo.getReporter().getUsername());
        assertEquals(TEST_USERNAME, issueInfo2.getReporter().getUsername());
    }

    @Test
    void existsByTitleIgnoreCase() {
        boolean normalCaseBool = issueRepository.existsByTitleIgnoreCase(TEST_ISSUE_TITLE);
        boolean upperCaseBool = issueRepository.existsByTitleIgnoreCase(TEST_ISSUE_TITLE.toUpperCase());
        boolean lowerCaseBool = issueRepository.existsByTitleIgnoreCase(TEST_ISSUE_TITLE.toLowerCase());

        assertTrue(normalCaseBool);
        assertTrue(upperCaseBool);
        assertTrue(lowerCaseBool);
    }

}
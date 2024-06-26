package ovh.kkazm.bugtracker.issue;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ovh.kkazm.bugtracker.issue.IssueService.IssueInfo;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Query("select i from Issue i join fetch i.reporter join fetch i.project where i.project.id = ?1")
    Page<IssueInfo> getAllProjectIssues(Long projectId, Pageable pageable);

    boolean existsByTitleIgnoreCase(String title);

}

package ovh.kkazm.bugtracker.issue;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ovh.kkazm.bugtracker.user.User;

import java.time.ZonedDateTime;
import java.util.Optional;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Query("select i from Issue i join fetch i.reporter join fetch i.project where i.project.id = ?1")
    Page<IssueInfo> getAllProjectIssues(Long projectId, Pageable pageable);

    boolean existsByTitleIgnoreCase(String title);

//    @AuthorizeReturnObject // TODO
    Optional<IssueInfo> findIssueInfoById(Long id);

    /**
     * Projection for {@link Issue}
     */
    interface IssueInfo {
        Long getId();

        String getTitle();

        String getDescription();

        ZonedDateTime getCreatedAt();

        ZonedDateTime getUpdatedAt();

        UserInfo getReporter();

        ProjectInfo getProject();

        /**
         * Projection for {@link User}
         */
        interface UserInfo {
            Long getId();

            String getUsername();
        }

        /**
         * Projection for {@link ovh.kkazm.bugtracker.project.Project}
         */
        interface ProjectInfo {
            Long getId();

            String getName();

            ZonedDateTime getCreatedAt();
        }
    }
}

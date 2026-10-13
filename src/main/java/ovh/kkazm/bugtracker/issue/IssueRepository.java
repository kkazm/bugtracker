package ovh.kkazm.bugtracker.issue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface IssueRepository extends JpaRepository<Issue, Long>, JpaSpecificationExecutor<Issue> {

    /**
     * Delete all Issues which belong to a Project with the given ID.
     */
    @Modifying
    @Query("delete from Issue i where i.project.id = :projectId")
    void deleteByProjectId(Long projectId);

}

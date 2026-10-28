package ovh.kkazm.bugtracker.project;

import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {

    boolean existsByName(String name);

    @NonNull
    @EntityGraph(attributePaths = {"issues"})
    Page<Project> findDetailedAllBy(@NonNull Pageable pageable);

    @Modifying
    @Query("delete from Project p where p.id = :id")
    void deleteProjectById(@NonNull Long id);

}

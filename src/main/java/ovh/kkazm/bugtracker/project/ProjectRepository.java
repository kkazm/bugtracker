package ovh.kkazm.bugtracker.project;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {

    boolean existsByName(String name);

    @EntityGraph(attributePaths = {"owner"})
    @NonNull
    Optional<Project> findById(@NonNull Long id);

}

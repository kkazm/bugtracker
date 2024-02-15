package ovh.kkazm.bugtracker.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ovh.kkazm.bugtracker.project.ProjectService.ProjectInfo;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<ProjectInfo> findBy(Pageable pageable);

}
package ovh.kkazm.bugtracker.projectmembership;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProjectMembershipRepository extends JpaRepository<ProjectMembership, Long>, JpaSpecificationExecutor<ProjectMembership> {
}

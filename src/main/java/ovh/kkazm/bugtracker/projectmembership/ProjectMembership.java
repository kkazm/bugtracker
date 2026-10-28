package ovh.kkazm.bugtracker.projectmembership;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ovh.kkazm.bugtracker.project.Project;
import ovh.kkazm.bugtracker.user.User;

@Entity
@Table(name = "project_memberships",
        uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "user_id"}))
@Getter
@Setter
public class ProjectMembership {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectRole role;

}

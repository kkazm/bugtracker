package ovh.kkazm.bugtracker.project;

import jakarta.persistence.*;
import lombok.*;
import ovh.kkazm.bugtracker.issue.Issue;
import ovh.kkazm.bugtracker.user.User;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "project")
    private Set<Issue> issues = new LinkedHashSet<>();

}

package ovh.kkazm.bugtracker.project;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ovh.kkazm.bugtracker.user.User;

@Entity
@Table(name = "project")

@Getter
@Setter
public class Project {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

}
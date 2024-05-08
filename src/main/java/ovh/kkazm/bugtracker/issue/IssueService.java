package ovh.kkazm.bugtracker.issue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ovh.kkazm.bugtracker.project.Project;
import ovh.kkazm.bugtracker.project.ProjectRepository;
import ovh.kkazm.bugtracker.user.User;
import ovh.kkazm.bugtracker.user.UserRepository;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
//@Validated // TODO
public class IssueService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final IssueRepository issueRepository;
    private final IssueMapper issueMapper;

    @Transactional
    public IssueDto createIssue(@Valid IssueDto issueDto, Authentication authentication) {
        Issue issue = new Issue();

        if (issueRepository.existsByTitleIgnoreCase(issueDto.title())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Issue with this title already exists.");
        }
        issue.setTitle(issueDto.title());

        issue.setDescription(issueDto.description());

        Project projectReferenceById = projectRepository.getReferenceById(issueDto.projectId());
        issue.setProject(projectReferenceById);

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid reporter username"));
        issue.setReporter(user);

        String assigneeUsername = issueDto.assigneeUsername();
        if (assigneeUsername != null && !assigneeUsername.trim().isEmpty()) {
            User assignee = userRepository.findByUsername(assigneeUsername)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid assignee username"));
            issue.setAssignee(assignee);
        }

        Issue savedIssue = issueRepository.save(issue);
        return issueMapper.toDto(savedIssue);
    }

    /**
     * DTO for {@link Issue}
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record IssueDto(
            @NotNull @NotBlank
            String title,
            @Size(max = 4096)
            String description,
            @NotNull @Positive
            Long projectId,
            String assigneeUsername
    ) implements Serializable {
    }

    /**
     * Projection for {@link Issue}
     */
    public interface IssueInfo {
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

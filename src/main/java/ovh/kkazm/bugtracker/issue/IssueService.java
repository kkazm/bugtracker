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
    public CreateIssueDto
    createIssue(@Valid CreateIssueDto createIssueDto, Authentication authentication) {
        Issue issue = new Issue();

        if (issueRepository.existsByTitleIgnoreCase(createIssueDto.title())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An Issue with this title already exists.");
        }
        issue.setTitle(createIssueDto.title());

        issue.setDescription(createIssueDto.description());

        Project project = projectRepository.findById(createIssueDto.projectId()) // TODO getReferenceById?
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "A Project with this Id does not exist"));
        issue.setProject(project);

        User user = userRepository.findByUsername(authentication.getName()) // TODO
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid reporter username"));
        issue.setReporter(user);

        // TODO authorization: Assignee must be a member of the project
        String assigneeUsername = createIssueDto.assigneeUsername();
        if (assigneeUsername != null && !assigneeUsername.trim().isEmpty()) {
            User assignee = userRepository.findByUsername(assigneeUsername)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid assignee username"));
            issue.setAssignee(assignee);
        }

        Issue savedIssue = issueRepository.save(issue);
        return issueMapper.toDto(savedIssue);
    }

    /**
     * @throws java.util.NoSuchElementException
     */
    @Transactional
    public IssueInfo
    getIssue(Long issueId) {
        final var issueById = issueRepository.findIssueInfoById(issueId);
        return issueById.orElseThrow();
    }

    /**
     * DTO for {@link Issue}
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CreateIssueDto(
            @NotNull @NotBlank
            String title,
            @Size(max = 4096)
            String description,
            @NotNull @Positive
            Long projectId,
            @NotBlank
            String assigneeUsername
    ) implements Serializable { // TODO
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

package ovh.kkazm.bugtracker.issue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.kkazm.bugtracker.project.Project;
import ovh.kkazm.bugtracker.project.ProjectRepository;
import ovh.kkazm.bugtracker.user.User;
import ovh.kkazm.bugtracker.user.UserRepository;

import java.io.Serializable;

@Service
@RequiredArgsConstructor
//@Validated // TODO
public class IssueService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final IssueRepository issueRepository;
    private final IssueMapper issueMapper;

    @Transactional
    public IssueDto
    createIssue(@Valid IssueService.IssueDto issueDto) {
        Issue issue = new Issue();

        if (issueRepository.existsByTitleIgnoreCase(issueDto.title())) {
            throw new IllegalStateException("An Issue with this title already exists.");
        }
        issue.setTitle(issueDto.title());

        issue.setDescription(issueDto.description());

        Project project = projectRepository.findById(issueDto.projectId()) // TODO getReferenceById?
                .orElseThrow(() -> new IllegalArgumentException("A Project with this Id does not exist"));
        issue.setProject(project);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()) // TODO
                .orElseThrow(() -> new IllegalArgumentException("Invalid reporter username"));
        issue.setReporter(user);

        // TODO authorization: Assignee must be a member of the project
        String assigneeUsername = issueDto.assigneeUsername();
        if (assigneeUsername != null && !assigneeUsername.trim().isEmpty()) {
            User assignee = userRepository.findByUsername(assigneeUsername)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid assignee username"));
            issue.setAssignee(assignee);
        }

        Issue savedIssue = issueRepository.save(issue);
        return issueMapper.toDto(savedIssue);
    }

    /**
     * @throws java.util.NoSuchElementException if no value present
     */
    @Transactional
    public IssueRepository.IssueInfo
    getIssue(Long issueId) {
        final var issueById = issueRepository.findIssueInfoById(issueId);
        return issueById.orElseThrow();
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
            @NotBlank
            String assigneeUsername
    ) implements Serializable { // TODO
    }

}

package ovh.kkazm.bugtracker.issue;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;
import ovh.kkazm.bugtracker.project.Project;
import ovh.kkazm.bugtracker.project.ProjectRepository;
import ovh.kkazm.bugtracker.user.User;
import ovh.kkazm.bugtracker.user.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated // TODO
public class IssueService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final IssueRepository issueRepository;
    private final IssueMapper issueMapper;

    @Transactional
    public IssueDto createIssue(@Valid IssueDto issueDto, Authentication authentication) {
        Issue issue = new Issue();
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

}

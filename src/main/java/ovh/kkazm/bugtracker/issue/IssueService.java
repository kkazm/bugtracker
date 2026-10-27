package ovh.kkazm.bugtracker.issue;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.kkazm.bugtracker.exceptions.ResourceNotFoundException;
import ovh.kkazm.bugtracker.issue.dtos.CreateIssueDto;
import ovh.kkazm.bugtracker.issue.dtos.IssueCreatedDto;
import ovh.kkazm.bugtracker.issue.dtos.IssueSummaryDto;
import ovh.kkazm.bugtracker.project.Project;
import ovh.kkazm.bugtracker.project.ProjectRepository;
import ovh.kkazm.bugtracker.user.User;
import ovh.kkazm.bugtracker.user.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class IssueService {
    private final IssueMapper issueMapper;
    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional
    IssueCreatedDto createIssue(CreateIssueDto inputDto, String reporterUsername) {
        Project project = projectRepository.findById(inputDto.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project", inputDto.projectId()));
        User reporter = userRepository.findByUsername(reporterUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User: ", reporterUsername));
        Issue issue = issueMapper.toEntity(inputDto);
        issue.setProject(project);
        issue.setReporter(reporter);
        Issue savedIssue = issueRepository.save(issue);
        return issueMapper.toIssueCreatedDto(savedIssue);
    }

    public PagedModel<IssueSummaryDto>
    getAPageOfIssues(Long projectId, Pageable pageable) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project", projectId);
        }

        Page<IssueSummaryDto> page =
                issueRepository.findByProject_Id(projectId, pageable)
                        .map(issueMapper::toIssueSummaryDto);
        return new PagedModel<>(page);
    }

    public IssueSummaryDto get(Long id) {
        Optional<Issue> optionalIssue = issueRepository.findById(id);
        Issue issue = optionalIssue.orElseThrow(() -> new ResourceNotFoundException("Issue", id));
        return issueMapper.toIssueSummaryDto(issue);
    }

    public void delete(Long id) {
        issueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issue", id));
        issueRepository.deleteById(id);
    }
}

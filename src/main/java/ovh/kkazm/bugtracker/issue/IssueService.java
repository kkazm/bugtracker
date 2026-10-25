package ovh.kkazm.bugtracker.issue;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.kkazm.bugtracker.exceptions.ResourceNotFoundException;
import ovh.kkazm.bugtracker.issue.dtos.CreateIssueDto;
import ovh.kkazm.bugtracker.issue.dtos.IssueCreatedDto;
import ovh.kkazm.bugtracker.project.Project;
import ovh.kkazm.bugtracker.project.ProjectRepository;

@Service
@RequiredArgsConstructor
class IssueService {
    private final IssueMapper issueMapper;
    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    IssueCreatedDto createIssue(CreateIssueDto inputDto) {
        Project project = projectRepository.findById(inputDto.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project", inputDto.projectId()));
        Issue issue = issueMapper.toEntity(inputDto);
        issue.setProject(project);
        Issue savedIssue = issueRepository.save(issue);
        return issueMapper.toIssueCreatedDto(savedIssue);
    }

}

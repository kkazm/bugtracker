package ovh.kkazm.bugtracker.issue;

import org.mapstruct.*;
import ovh.kkazm.bugtracker.issue.dtos.CreateIssueDto;
import ovh.kkazm.bugtracker.issue.dtos.IssueCreatedDto;
import ovh.kkazm.bugtracker.issue.dtos.IssueSummaryDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IssueMapper {
    @Mapping(source = "projectId", target = "project.id")
    Issue toEntity(CreateIssueDto createIssueDto);

    @Mapping(source = "projectId", target = "project.id")
    Issue toEntity(IssueCreatedDto issueCreatedDto);

    Issue toEntity(IssueSummaryDto issue);

    @Mapping(source = "project.id", target = "projectId")
    CreateIssueDto toCreateIssueDto(Issue issue);

    @Mapping(source = "project.id", target = "projectId")
    IssueCreatedDto toIssueCreatedDto(Issue issue);

    IssueSummaryDto toIssueSummaryDto(Issue issue);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "projectId", target = "project.id")
    Issue partialUpdate(IssueCreatedDto issueCreatedDto, @MappingTarget Issue issue);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "projectId", target = "project.id")
    Issue partialUpdate(CreateIssueDto createIssueDto, @MappingTarget Issue issue);
}

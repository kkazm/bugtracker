package ovh.kkazm.bugtracker.issue;

import org.mapstruct.*;
import ovh.kkazm.bugtracker.issue.IssueService.CreateIssueDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IssueMapper {
    @Mapping(source = "assigneeUsername", target = "assignee.username")
    @Mapping(source = "projectId", target = "project.id")
    Issue toEntity(CreateIssueDto createIssueDto);

    @InheritInverseConfiguration(name = "toEntity")
    CreateIssueDto toDto(Issue issue);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Issue partialUpdate(IssueService.CreateIssueDto createIssueDto, @MappingTarget Issue issue);
}
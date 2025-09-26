package ovh.kkazm.bugtracker.issue;

import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IssueMapper {
    @Mapping(source = "assigneeUsername", target = "assignee.username")
    @Mapping(source = "projectId", target = "project.id")
    Issue toEntity(IssueService.IssueDto issueDto);

    @InheritInverseConfiguration(name = "toEntity")
    IssueService.IssueDto toDto(Issue issue);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Issue partialUpdate(IssueService.IssueDto issueDto, @MappingTarget Issue issue);
}
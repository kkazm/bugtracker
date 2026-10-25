package ovh.kkazm.bugtracker.project;

import org.mapstruct.*;
import ovh.kkazm.bugtracker.project.dtos.ProjectCreatedDto;
import ovh.kkazm.bugtracker.project.dtos.ProjectDetailDto;
import ovh.kkazm.bugtracker.project.dtos.ProjectDto;
import ovh.kkazm.bugtracker.user.UserMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public interface ProjectMapper {

    ProjectCreatedDto toDto(Project project);

    ProjectDetailDto toDetailDto(Project project);

    ProjectDto toProjectDto(Project project);

    Project toEntity(ProjectCreatedDto projectCreatedDto);

    Project toEntity(ProjectDto projectDto);

    Project toEntity(ProjectDetailDto projectDetailDto);

    // TODO
    @AfterMapping
    default void linkIssues(@MappingTarget Project project) {
        project.getIssues().forEach(issue -> issue.setProject(project));
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Project partialUpdate(ProjectDto projectDto, @MappingTarget Project project);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Project partialUpdate(ProjectDetailDto projectDetailDto, @MappingTarget Project project);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Project partialUpdate(ProjectCreatedDto projectCreatedDto, @MappingTarget Project project);
}

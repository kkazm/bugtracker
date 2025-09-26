package ovh.kkazm.bugtracker.project;

import org.mapstruct.*;
import ovh.kkazm.bugtracker.project.ProjectService.CreateProjectDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectMapper {

    Project toEntity(ProjectService.CreateProjectDto createProjectDto);

    CreateProjectDto toDto(Project project);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Project partialUpdate(ProjectService.CreateProjectDto createProjectDto, @MappingTarget Project project);

}
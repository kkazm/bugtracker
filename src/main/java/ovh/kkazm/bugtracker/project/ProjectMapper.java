package ovh.kkazm.bugtracker.project;

import org.mapstruct.*;
import ovh.kkazm.bugtracker.project.dtos.ProjectCreatedDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectMapper {

    Project toEntity(ProjectCreatedDto projectCreatedDto);

    ProjectCreatedDto toDto(Project project);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Project partialUpdate(ProjectCreatedDto projectCreatedDto, @MappingTarget Project project);
}

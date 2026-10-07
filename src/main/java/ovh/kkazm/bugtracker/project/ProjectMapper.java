package ovh.kkazm.bugtracker.project;

import org.mapstruct.*;
import ovh.kkazm.bugtracker.project.dtos.ProjectCreatedDto;
import ovh.kkazm.bugtracker.project.dtos.ProjectDto;
import ovh.kkazm.bugtracker.user.UserMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public interface ProjectMapper {

//    Project toEntity(ProjectCreatedDto projectCreatedDto);

    ProjectCreatedDto toDto(Project project);

    ProjectDto toDto1(Project project);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Project partialUpdate(ProjectDto projectDto, @MappingTarget Project project);

    Project toEntity(ProjectDto projectDto);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    Project partialUpdate(ProjectCreatedDto projectCreatedDto, @MappingTarget Project project);
}

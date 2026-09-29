package ovh.kkazm.bugtracker.project.events;

import org.springframework.context.ApplicationEvent;
import ovh.kkazm.bugtracker.project.dtos.ProjectCreatedDto;

public class ProjectCreatedEvent extends ApplicationEvent {

    private final ProjectCreatedDto projectCreatedDto;

    public ProjectCreatedEvent(Object source, ProjectCreatedDto projectCreatedDto) {
        super(source);
        this.projectCreatedDto = projectCreatedDto;
    }

}

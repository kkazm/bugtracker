package ovh.kkazm.bugtracker.project;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ovh.kkazm.bugtracker.project.events.ProjectCreatedEvent;

@Slf4j
@Component
public class ProjectEventsListener {

    @EventListener
    public void projectCreatedEvent(ProjectCreatedEvent event) {
        log.info("event = {}", event);
    }

}

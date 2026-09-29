package ovh.kkazm.bugtracker.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ovh.kkazm.bugtracker.project.dtos.CreateProjectDto;
import ovh.kkazm.bugtracker.project.dtos.ProjectCreatedDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTests {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    @Test
    void createProjectReturnsCreatedResponseWithLocationAndProject() {
        CreateProjectDto request = new CreateProjectDto("TestProject");
        ProjectCreatedDto createdProject = new ProjectCreatedDto(11L, "TestProject");
        when(projectService.create(request)).thenReturn(createdProject);

        var response = projectController.createProject(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("/projects/11", response.getHeaders().getLocation().toString());
        assertSame(createdProject, response.getBody());
        verify(projectService).create(request);
    }
}

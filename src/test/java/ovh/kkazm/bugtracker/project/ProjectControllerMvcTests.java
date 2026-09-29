package ovh.kkazm.bugtracker.project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ovh.kkazm.bugtracker.project.dtos.CreateProjectDto;
import ovh.kkazm.bugtracker.project.dtos.ProjectCreatedDto;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
class ProjectControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @Test
    void createProjectReturnsCreatedResource() throws Exception {
        CreateProjectDto request = new CreateProjectDto("TestProject");
        when(projectService.create(request)).thenReturn(new ProjectCreatedDto(11L, "TestProject"));

        mockMvc.perform(post("/projects")
                        .with(user("testUser"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"projectName":"TestProject"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/projects/11"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.name").value("TestProject"));

        verify(projectService).create(request);
    }

    @Test
    void createProjectRejectsNameShorterThanThreeCharacters() throws Exception {
        mockMvc.perform(post("/projects")
                        .with(user("testUser"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"projectName":"ab"}
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(projectService);
    }
}

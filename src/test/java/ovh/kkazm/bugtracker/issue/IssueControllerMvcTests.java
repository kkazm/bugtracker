package ovh.kkazm.bugtracker.issue;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ovh.kkazm.bugtracker.issue.dtos.CreateIssueDto;
import ovh.kkazm.bugtracker.issue.dtos.IssueCreatedDto;
import ovh.kkazm.bugtracker.issue.dtos.IssueSummaryDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IssueController.class)
class IssueControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IssueService issueService;

    @Test
    void createIssueReturnsCreatedResourceAndPassesAuthenticatedUsername() throws Exception {
        CreateIssueDto request = new CreateIssueDto("Login fails", 17L);
        when(issueService.createIssue(request, "testUser"))
                .thenReturn(new IssueCreatedDto(31L, "Login fails", 17L));

        mockMvc.perform(post("/issues")
                        .with(user("testUser"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Login fails","projectId":17}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/issues/31"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(31))
                .andExpect(jsonPath("$.title").value("Login fails"))
                .andExpect(jsonPath("$.projectId").value(17));

        verify(issueService).createIssue(request, "testUser");
    }

    @Test
    void createIssueRejectsBlankTitle() throws Exception {
        mockMvc.perform(post("/issues")
                        .with(user("testUser"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":" ","projectId":17}
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(issueService);
    }

    @Test
    void getAPageOfIssuesDefaultsToNewestFirst() throws Exception {
        when(issueService.getAPageOfIssues(eq(17L), any(Pageable.class)))
                .thenReturn(new PagedModel<>(Page.<IssueSummaryDto>empty()));

        mockMvc.perform(get("/projects/17/issues").with(user("testUser")))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(issueService).getAPageOfIssues(eq(17L), pageableCaptor.capture());
        assertEquals(Sort.by(Sort.Direction.DESC, "createdAt"), pageableCaptor.getValue().getSort());
    }

    @Test
    void getAPageOfIssuesPassesRequestedPaginationAndSort() throws Exception {
        when(issueService.getAPageOfIssues(eq(17L), any(Pageable.class)))
                .thenReturn(new PagedModel<>(Page.<IssueSummaryDto>empty()));

        mockMvc.perform(get("/projects/17/issues")
                        .with(user("testUser"))
                        .param("page", "2")
                        .param("size", "5")
                        .param("sort", "title,asc"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(issueService).getAPageOfIssues(eq(17L), pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        assertEquals(2, pageable.getPageNumber());
        assertEquals(5, pageable.getPageSize());
        assertEquals(Sort.by(Sort.Direction.ASC, "title"), pageable.getSort());
    }
}

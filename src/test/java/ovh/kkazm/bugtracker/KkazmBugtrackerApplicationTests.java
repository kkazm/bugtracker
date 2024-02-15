package ovh.kkazm.bugtracker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
// TODO
@AutoConfigureMockMvc
class KkazmBugtrackerApplicationTests {

    // TODO
    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    void whenUnauthenticatedThenForbidden() throws Exception {
        this.mockMvc.perform(get("/hello"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void whenAuthenticatedThenOk() throws Exception {
        this.mockMvc.perform(get("/hello"))
                .andExpect(status().isOk());
    }

}

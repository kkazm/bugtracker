package ovh.kkazm.bugtracker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
//@SpringBootTest(webEnvironment = RANDOM_PORT)
//@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
//@WebMvcTest
//@DirtiesContext
//@TestInstance
class KkazmBugtrackerApplicationTests {

    @Autowired
    private MockMvc mockMvc;
//    @LocalServerPort
//    private int port;
//    @Autowired
//    private TestRestTemplate restTemplate;

    @Test
    @WithMockUser
    void contextLoads() throws Exception {
        this.mockMvc.perform(get("/projects"))
                .andDo(print())
                .andExpect(status().isOk());
//                .andExpect(content().string(containsString("Hello, World")))
    }

    @Test
    void whenUnauthenticatedThenForbidden() throws Exception {
        this.mockMvc.perform(post("/projects"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void whenAuthenticatedThenOk() throws Exception {
        this.mockMvc.perform(post("/projects"))
                .andExpect(status().isBadRequest());
    }

}

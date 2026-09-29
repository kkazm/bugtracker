package ovh.kkazm.bugtracker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ovh.kkazm.bugtracker.project.ProjectService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = RANDOM_PORT)
//@SpringBootTest(webEnvironment = MOCK)
//@AutoConfigureMockMvc
//@WebMvcTest
//@DirtiesContext
//@TestInstance

//@DataJpaTest
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)

class KkazmBugtrackerApplicationTests implements ApplicationContextAware {

    private ApplicationContext applicationContext;

//    @Autowired
//    private MockMvc mockMvc;
////    @LocalServerPort
////    private int port;
////    @Autowired
////    private TestRestTemplate restTemplate;

    @Test
    void contextLoads(ApplicationContext context) {
        System.out.println("context = " + context);
        context.getBeanDefinitionCount();
        context.getBeanDefinitionNames();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

//    @Test
//    @WithMockUser
//    void contextLoads() throws Exception {
//        this.mockMvc.perform(get("/projects"))
//                .andDo(print())
//                .andExpect(status().isOk());
////                .andExpect(content().string(containsString("Hello, World")))
//    }
//
//    @Test
//    void whenUnauthenticatedThenForbidden() throws Exception {
//        this.mockMvc.perform(post("/projects"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockUser
//    void whenAuthenticatedThenOk() throws Exception {
//        this.mockMvc.perform(post("/projects"))
//                .andExpect(status().isBadRequest());
//    }

}

package ovh.kkazm.bugtracker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@SpringBootTest
@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = RANDOM_PORT)
//@SpringBootTest(webEnvironment = MOCK)
//@AutoConfigureMockMvc
//@WebMvcTest
//@DirtiesContext
//@TestInstance
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

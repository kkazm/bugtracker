package ovh.kkazm.bugtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(proxyBeanMethods = false)
//@EnableAspectJAutoProxy // TODO
//@EnableLoadTimeWeaving
//@EnableSpringConfigured
//@EnableWebMvc // Do not use in a spring boot application
//@ComponentScan(basePackages = {"ovh.kkazm.hello", "ovh.kkazm.bugtracker"})
//        excludeFilters = @Filter(type = FilterType.ANNOTATION, pattern = "somepattern"),
//        includeFilters = {@Filter(type = FilterType.ANNOTATION, pattern = "somepattern2"),
//            @Filter(classes = Banner.class, type = FilterType.CUSTOM)})
public class KkazmBugtrackerApplication {

    public static void main(String[] args) {
//        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder();
        SpringApplication application = new SpringApplication(KkazmBugtrackerApplication.class);
//        application.setApplicationStartup(new FlightRecorderApplicationStartup());
        ConfigurableApplicationContext run = application.run(args);
    }

}

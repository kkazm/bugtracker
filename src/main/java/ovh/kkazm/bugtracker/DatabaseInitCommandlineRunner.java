package ovh.kkazm.bugtracker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Profile({"devel"})
public class DatabaseInitCommandlineRunner {

    @Bean
    public CommandLineRunner
    databaseInitCommandLineRunner() {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                System.out.println("Running first command line runner...");
            }
        };
    }

}

package ovh.kkazm.bugtracker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import ovh.kkazm.bugtracker.issue.Issue;
import ovh.kkazm.bugtracker.issue.IssueRepository;
import ovh.kkazm.bugtracker.project.Project;
import ovh.kkazm.bugtracker.project.ProjectRepository;
import ovh.kkazm.bugtracker.user.User;
import ovh.kkazm.bugtracker.user.UserRepository;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Profile({"devel"})
public class DatabaseInitCommandlineRunner {

    @Bean
    public CommandLineRunner
    databaseInitCommandLineRunner(IssueRepository issueRepository,
                                  ProjectRepository projectRepository,
                                  UserRepository userRepository,
                                  ObjectMapper objectMapper) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                System.out.println("Running first command line runner...");
                User user = new User();
                user.setUsername("Konrad");
                userRepository.save(user);

                User user2 = new User();
                user2.setUsername("Marta");
                userRepository.save(user2);

                User user3 = new User();
                user3.setUsername("Zygmunt");
                userRepository.save(user3);

                User user4 = new User();
                user4.setUsername("Stasio");
                userRepository.save(user4);

                Project project1 = new Project();
                project1.setName("First project");
                project1.setOwner(user);

                Project project2 = new Project();
                project2.setName("Second project");
                project2.setOwner(user2);

                Project project3 = new Project();
                project3.setName("Third project");
                project3.setOwner(user3);

                Project project4 = new Project();
                project4.setName("Fourth project");
                project4.setOwner(user4);

                List<Project> projects1 = new ArrayList<>(120);
                for (int i = 0; i < 100; i++) {
                    Project project = new Project();
                    project.setName("Project " + i);
                    project.setOwner(user4);
                    projects1.add(project);
                }
                projectRepository.saveAll(projects1);

                List<Project> projects = new ArrayList<>();
                projects.add(project1);
                projects.add(project2);
                projects.add(project3);
                projects.add(project4);
                projectRepository.saveAll(projects);


                Issue issue1 = new Issue();
                issue1.setTitle("My first issue");
                issue1.setDescription("Example description");
                issue1.setProject(project1);
                issue1.setReporter(user);

                Issue issue4 = new Issue();
                issue4.setTitle("My fourth issue");
                issue4.setProject(project4);
                issue4.setReporter(user2);

                Issue issue5 = new Issue();
                issue5.setTitle("My fifth issue");
                issue5.setProject(project4);
                issue5.setReporter(user3);

                Issue issue6 = new Issue();
                issue6.setTitle("My sixth issue");
                issue6.setProject(project4);
                issue6.setReporter(user3);
                issue6.setDescription("Lorem, ipsum dolor sit amet consectetur adipisicing elit. " +
                        "Repellat vero quibusdam minima ad sequi? Voluptas maxime ipsum perspiciatis officia" +
                        " mollitia aliquam error, blanditiis id atque magnam reiciendis itaque labore quasi?");
                issueRepository.save(issue1);
                issueRepository.save(issue4);
                issueRepository.save(issue5);
                issueRepository.save(issue6);

                List<Issue> issues = new ArrayList<>(120);
                for (int i = 0; i < 100; i++) {
                    Issue issue = new Issue();
                    issue.setTitle("My issue nr " + i);
                    issue.setProject(project4);
                    issue.setReporter(user3);
                    issue.setDescription("Lorem, ipsum dolor sit amet consectetur adipisicing elit. " +
                            "Repellat vero quibusdam minima ad sequi? Voluptas maxime ipsum perspiciatis officia" +
                            " mollitia aliquam error, blanditiis id atque magnam reiciendis itaque labore quasi?");
                    issues.add(issue);
                }
                issueRepository.saveAll(issues);

                System.out.println("Completed first commandline runner");
            }
        };
    }

}

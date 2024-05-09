package ovh.kkazm.bugtracker;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ovh.kkazm.bugtracker.issue.Issue;
import ovh.kkazm.bugtracker.issue.IssueRepository;
import ovh.kkazm.bugtracker.project.Project;
import ovh.kkazm.bugtracker.project.ProjectRepository;
import ovh.kkazm.bugtracker.user.User;
import ovh.kkazm.bugtracker.user.UserRepository;

/**
 * TODO Do not use inheritance here?
 */
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CommonDatabaseSetup {

    public static final String TEST_USERNAME = "Test username";
    public static final String TEST_USERNAME_2 = "Test username 2";
    public static final String TEST_PROJECT_NAME = "Test project";
    public static final String TEST_PROJECT_NAME_2 = "Test project 2";
    public static final String TEST_ISSUE_TITLE = "Test issue title";
    public static final String TEST_ISSUE_TITLE_2 = "Test issue title 2";
    @Autowired
    protected IssueRepository issueRepository;
    @Autowired
    protected ProjectRepository projectRepository;
    @Autowired
    protected UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUsername(TEST_USERNAME);
        userRepository.save(user);

            User user2 = new User();
            user2.setUsername(TEST_USERNAME_2);
            userRepository.save(user2);

        Project project = new Project();
        project.setName(TEST_PROJECT_NAME);
        project.setOwner(user);
        projectRepository.save(project);

            Project project2 = new Project();
            project2.setName(TEST_PROJECT_NAME_2);
            project2.setOwner(user2);
            projectRepository.save(project2);

        Issue issue = new Issue();
        issue.setTitle(TEST_ISSUE_TITLE);
        issue.setProject(project);
        issue.setReporter(user);
        issueRepository.save(issue);

            Issue issue2 = new Issue();
            issue2.setTitle(TEST_ISSUE_TITLE_2);
            issue2.setProject(project);
            issue2.setReporter(user2);
            issueRepository.save(issue2);
    }
}

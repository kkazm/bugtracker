package ovh.kkazm.bugtracker.project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ovh.kkazm.bugtracker.CommonDatabaseSetup;
import ovh.kkazm.bugtracker.project.ProjectService.ProjectInfo;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ProjectRepositoryTest extends CommonDatabaseSetup {

    @Test
    void findBy() {
        Pageable page = PageRequest.of(0, 20, Sort.by(Sort.Direction.ASC, "name"));

        Page<ProjectInfo> projectInfoPage = projectRepository.findBy(page);

        assertEquals(2, projectInfoPage.getTotalElements());
        ProjectInfo projectInfo = projectInfoPage.getContent().get(0);
        ProjectInfo projectInfo2 = projectInfoPage.getContent().get(1);
        assertEquals(TEST_PROJECT_NAME, projectInfo.getName());
        assertEquals(TEST_USERNAME, projectInfo.getOwner().getUsername());
        assertEquals(TEST_PROJECT_NAME_2, projectInfo2.getName());
        assertEquals(TEST_USERNAME_2, projectInfo2.getOwner().getUsername());
    }

    @Test
    void existsByNameIgnoreCase() {
        boolean normalCaseBool = projectRepository.existsByNameIgnoreCase(TEST_PROJECT_NAME);
        boolean lowerCaseBool = projectRepository.existsByNameIgnoreCase(TEST_PROJECT_NAME.toLowerCase());
        boolean upperCaseBool = projectRepository.existsByNameIgnoreCase(TEST_PROJECT_NAME.toUpperCase());
        boolean bad_upperCaseBool = projectRepository.existsByNameIgnoreCase(TEST_PROJECT_NAME.toUpperCase() + " bad string example");
        boolean bad_lowerCaseBool = projectRepository.existsByNameIgnoreCase(TEST_PROJECT_NAME.toLowerCase() + " bad string example");

        assertTrue(normalCaseBool);
        assertTrue(lowerCaseBool);
        assertTrue(upperCaseBool);
        assertFalse(bad_lowerCaseBool);
        assertFalse(bad_upperCaseBool);
    }

}
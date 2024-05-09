package ovh.kkazm.bugtracker.user;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ovh.kkazm.bugtracker.CommonDatabaseSetup;
import ovh.kkazm.bugtracker.user.UserService.UserInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRepositoryTest extends CommonDatabaseSetup {

    @Test
    void findBy() {
        Pageable page = PageRequest.of(
                0,
                20,
                Sort.by(Sort.Direction.ASC, "username"));

        Page<UserInfo> allUsersPage = userRepository.findBy(page);

        assertEquals(2, allUsersPage.getTotalElements());
        assertEquals(TEST_USERNAME, allUsersPage.getContent().get(0).getUsername());
        assertEquals(TEST_USERNAME_2, allUsersPage.getContent().get(1).getUsername());
    }

    @Test
    void findByUsername() {
        User user1 = userRepository.findByUsername(TEST_USERNAME).orElseThrow();
        User user2 = userRepository.findByUsername(TEST_USERNAME_2).orElseThrow();

        assertEquals(TEST_USERNAME, user1.getUsername());
        assertEquals(TEST_USERNAME_2, user2.getUsername());
    }

    @Test
    void existsByUsername() {
        boolean user1Exists = userRepository.existsByUsername(TEST_USERNAME);
        boolean user2Exists = userRepository.existsByUsername(TEST_USERNAME_2);

        assertTrue(user1Exists);
        assertTrue(user2Exists);
    }
}
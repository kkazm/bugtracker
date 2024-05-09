package ovh.kkazm.bugtracker.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ovh.kkazm.bugtracker.user.UserService.UserInfo;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * TODO findAll but return a projection?
     */
    Page<UserInfo> findBy(Pageable pageable);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

}
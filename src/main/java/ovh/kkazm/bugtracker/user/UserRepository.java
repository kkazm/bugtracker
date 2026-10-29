package ovh.kkazm.bugtracker.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ovh.kkazm.bugtracker.user.dtos.UserInfo;

import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Page<UserInfo> findBy(Pageable pageable);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

}

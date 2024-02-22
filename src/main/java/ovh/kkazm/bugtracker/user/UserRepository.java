package ovh.kkazm.bugtracker.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ovh.kkazm.bugtracker.user.UserService.UserInfo;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Page<UserInfo> findBy(Pageable pageable);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

}
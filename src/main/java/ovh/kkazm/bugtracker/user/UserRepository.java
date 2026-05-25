package ovh.kkazm.bugtracker.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long> {

    /**
     * TODO findAll but return a projection?
     */
    Page<UserInfo> findBy(Pageable pageable);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    /**
     * Projection for {@link User}
     */
    interface UserInfo {
        Long getId();

        String getUsername();

        ZonedDateTime getCreatedAt();
    }

}
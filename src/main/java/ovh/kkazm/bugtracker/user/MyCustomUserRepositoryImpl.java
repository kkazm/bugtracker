package ovh.kkazm.bugtracker.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Just for fun.
 */
@Slf4j
@RequiredArgsConstructor
public class MyCustomUserRepositoryImpl implements MyCustomUserRepository {

    private final EntityManagerFactory emf;
    // @PersistenceContext
    private final EntityManager em;

    @Override
    public String getRandoUser() {
/*
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory()) {
            EntityManager entityManager = emf.createEntityManager();
            List fromUserU = entityManager.createQuery("from User u").getResultList();
        }
*/
        Query fromUser2 = em.createQuery("from User");
        String string2 = fromUser2.getResultList().toString();

//        em.getTransaction().begin();
//        em.getTransaction().commit();
        try (EntityManager em = this.emf.createEntityManager()) {
            Query fromUser = em.createQuery("from User");
            String string = fromUser.getResultList().toString();
            log.info(string);
            return string;
        }
    }

}

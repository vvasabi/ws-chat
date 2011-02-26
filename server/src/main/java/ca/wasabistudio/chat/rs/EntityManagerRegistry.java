package ca.wasabistudio.chat.rs;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerRegistry {

    private static final String PERSISTENCE_UNIT = "chat";

    private static EntityManagerRegistry instance;

    private EntityManagerFactory emf;

    public EntityManagerRegistry() {
        if (instance != null) {
            throw new RuntimeException("Only one instance may be created.");
        }
    }

    public void initialize() {
        instance = this;
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
    }

    public void destroy() {
        emf.close();
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static EntityManagerRegistry instance() {
        return instance;
    }

}

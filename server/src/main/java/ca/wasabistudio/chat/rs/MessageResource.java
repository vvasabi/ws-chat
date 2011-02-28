package ca.wasabistudio.chat.rs;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Path;

@Path("/message")
public class MessageResource {

    private EntityManager em;

    public void setEntityManagerFactory(EntityManagerFactory emf) {
        em = emf.createEntityManager();
    }

    public void destroy() {
        em.close();
    }

}

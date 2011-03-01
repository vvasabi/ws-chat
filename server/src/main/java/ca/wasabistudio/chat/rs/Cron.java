package ca.wasabistudio.chat.rs;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import ca.wasabistudio.chat.entity.Client;

public class Cron {

    // 30 seconds
    private static final long TIMEOUT = 30000;

    private EntityManager em;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public void run() {
        Calendar time = Calendar.getInstance();
        long timeout = time.getTimeInMillis() - TIMEOUT;
        String query = "select c from Client where lastSync < :time";
        List<Client> clients = em.createQuery(query)
            .setParameter("time", new Date(timeout))
            .getResultList();
        for (Client client : clients) {
            em.remove(client);
        }
    }

}

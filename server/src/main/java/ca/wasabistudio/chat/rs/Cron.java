package ca.wasabistudio.chat.rs;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import ca.wasabistudio.chat.entity.Client;
import ca.wasabistudio.chat.entity.Room;

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
        Calendar now = Calendar.getInstance();
        Date expired = new Date(now.getTimeInMillis() - TIMEOUT);
        List<Room> rooms = em.createQuery("select r from Room r").getResultList();
        for (Room room : rooms) {
            for (Client client : room.getClients()) {
                if (expired.compareTo(client.getLastSync()) < 0) {
                    em.remove(client);
                }
            }
        }
    }

}

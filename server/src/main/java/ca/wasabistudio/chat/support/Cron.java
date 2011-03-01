package ca.wasabistudio.chat.support;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import ca.wasabistudio.chat.entity.Client;
import ca.wasabistudio.chat.entity.Message;
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
        String query = "select c from Client c where c.lastSync < :time";
        List<Client> clients = em.createQuery(query)
            .setParameter("time", expired)
            .getResultList();
        List<Room> rooms = em.createQuery("select r from Room r").getResultList();
        for (Client client : clients) {
            for (Room room : rooms) {
                room.removeClient(client);
            }
            for (Message message : client.getMessages()) {
                message.getRoom().removeMessage(message);
            }
            em.remove(client);
        }
    }

}

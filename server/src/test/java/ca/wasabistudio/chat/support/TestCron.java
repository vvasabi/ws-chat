package ca.wasabistudio.chat.support;

import static org.testng.Assert.*;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ca.wasabistudio.chat.entity.Client;
import ca.wasabistudio.chat.entity.Room;
import ca.wasabistudio.chat.support.Cron;

public class TestCron {

    private ClassPathXmlApplicationContext context;
    private EntityManagerFactory emf;

    @BeforeMethod
    public void setup() {
        String[] paths = new String[] {
                "META-INF/spring-jpa.xml",
                "META-INF/connector.xml",
                "META-INF/cron.xml",
                "META-INF/services.xml"
        };
        context = new ClassPathXmlApplicationContext(paths);
        emf = context.getBean(EntityManagerFactory.class);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Client client = new Client("moneycash");
        em.persist(client);
        Room room = new Room("room");
        em.persist(room);
        room.addClient(client);
        em.getTransaction().commit();
        em.close();
    }

    @AfterMethod
    public void tearDown() {
        emf.close();
        context.close();
    }

    @Test
    public void testRun() throws SecurityException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        // make client expire
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Calendar now = Calendar.getInstance();
        Date expired = new Date(now.getTimeInMillis() - 60000);
        Client client = em.find(Client.class, "moneycash");
        Field lastSync = client.getClass().getDeclaredField("lastSync");
        lastSync.setAccessible(true);
        lastSync.set(client, expired);
        em.getTransaction().commit();
        em.close();

        Cron cron = context.getBean(Cron.class);
        cron.run();

        // check that client is removed
        em = emf.createEntityManager();
        em.getTransaction().begin();
        client = em.find(Client.class, "moneycash");
        Room room = em.find(Room.class, "room");
        assertNull(client);
        assertEquals(room.getClients().size(), 0);
        em.getTransaction().commit();
        em.close();
    }

}

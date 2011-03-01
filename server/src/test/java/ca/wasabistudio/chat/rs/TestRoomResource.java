package ca.wasabistudio.chat.rs;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.testng.Assert.*;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ca.wasabistudio.chat.dto.MessageDTO;
import ca.wasabistudio.chat.entity.Client;
import ca.wasabistudio.chat.entity.Message;
import ca.wasabistudio.chat.entity.Room;
import ca.wasabistudio.chat.support.Session;

public class TestRoomResource {

    private ApplicationContext context;
    private EntityManagerFactory emf;

    @BeforeMethod
    public void setup() {
        String[] paths = new String[] {
                "META-INF/spring-jpa.xml",
                "META-INF/services.xml"
        };
        context = new ClassPathXmlApplicationContext(paths);
        emf = context.getBean(EntityManagerFactory.class);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Client client = new Client("moneycash");
        em.persist(client);
        Room room = new Room("room");
        Session session = context.getBean(Session.class);
        session.setClient(client);
        em.persist(room);
        em.getTransaction().commit();
        em.close();
    }

    @AfterMethod
    public void tearDown() {
        emf.close();
    }

    @Test
    public void testJoinRoom() {
        RoomResource resource = context.getBean(RoomResource.class);
        resource.joinRoom("room");

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Room room = em.find(Room.class, "room");
        Client client = em.find(Client.class, "moneycash");
        assertTrue(room.getClients().contains(client));
        assertNotNull(client.getRoomSetting(room));
        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void testGetMessages() {
        // join room first
        RoomResource resource = context.getBean(RoomResource.class);
        resource.joinRoom("room");

        // add message
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Room room = em.find(Room.class, "room");
        Client client = em.find(Client.class, "moneycash");
        Message message = new Message(client, room, "test message");
        room.addMessage(message);
        em.getTransaction().commit();
        em.close();

        // get messages now
        MessageDTO[] messages = resource.getMessages("room");
        assertEquals(messages.length, 1);
    }

}

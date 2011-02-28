package ca.wasabistudio.chat.rs;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.testng.Assert.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ca.wasabistudio.ca.chat.dto.MessageDTO;
import ca.wasabistudio.chat.entity.Client;
import ca.wasabistudio.chat.entity.Message;
import ca.wasabistudio.chat.entity.Room;

public class TestRoomResource {

    private EntityManagerFactory emf;

    @BeforeMethod
    public void setup() {
        String unit = "chat";
        emf = Persistence.createEntityManagerFactory(unit);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Client client = new Client("moneycash");
        em.persist(client);

        Room room = new Room("room");
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
        RoomResource resource = new RoomResource();
        resource.setEntityManagerFactory(emf);
        resource.joinRoom("room", "moneycash");
        resource.destroy();

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
        RoomResource resource = new RoomResource();
        resource.setEntityManagerFactory(emf);
        resource.joinRoom("room", "moneycash");
        resource.destroy();

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
        resource = new RoomResource();
        resource.setEntityManagerFactory(emf);
        MessageDTO[] messages = resource.getMessages("room", "moneycash");
        assertEquals(messages.length, 1);
        resource.destroy();
    }

}

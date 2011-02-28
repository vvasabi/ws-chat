package ca.wasabistudio.chat.entity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.testng.Assert.*;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestRoom {

    EntityManagerFactory emf;
    EntityManager em;

    @BeforeMethod
    public void setup() {
        String unit = "chat";
        emf = Persistence.createEntityManagerFactory(unit);
        em = emf.createEntityManager();

        em.getTransaction().begin();
        Client client = new Client("moneycash");
        em.persist(client);

        Room room = new Room("room");
        room.addClient(client);
        em.persist(room);
        em.getTransaction().commit();
    }

    @AfterMethod
    public void tearDown() {
        em.close();
        emf.close();
    }

    @Test
    public void testGetClients() {
        em.getTransaction().begin();
        Room room = (Room)em.find(Room.class, "room");
        Client client = (Client)em.find(Client.class, "moneycash");
        assertTrue(room.getClients().contains(client));
        em.getTransaction().commit();
    }

}

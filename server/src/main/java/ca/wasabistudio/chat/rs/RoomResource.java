package ca.wasabistudio.chat.rs;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import ca.wasabistudio.chat.entity.Client;
import ca.wasabistudio.chat.entity.Message;
import ca.wasabistudio.chat.entity.Room;
import ca.wasabistudio.chat.entity.RoomSetting;

@Path("/room")
public class RoomResource {

    private EntityManager em;

    public void setEntityManagerFactory(EntityManagerFactory emf) {
        em = emf.createEntityManager();
    }

    public void destroy() {
        em.close();
    }

    @GET
    @Path("/join/{room}/{client}")
    @Produces("application/json")
    public void joinRoom(@PathParam("room") String roomKey,
            @PathParam("client") String username) {
        em.getTransaction().begin();
        Client client = getClient(username);
        if (client == null) {
            em.getTransaction().commit();

            String message = "Client cannot be found.";
            throw new RequestErrorException(message);
        }

        Room room = getRoom(roomKey);
        if (room == null) {
            room = new Room(roomKey);
        }

        room.addClient(client);
        em.getTransaction().commit();
    }

    @SuppressWarnings("unchecked")
    @GET
    @Path("/messages/{room}/{client}")
    @Produces("application/json")
    public Message[] getMessages(@PathParam("room") String roomKey,
            @PathParam("client") String username) {
        em.getTransaction().begin();
        Client client = getClient(username);
        if (client == null) {
            em.getTransaction().commit();

            String message = "Client cannot be found.";
            throw new RequestErrorException(message);
        }

        Room room = getRoom(roomKey);
        if (room == null) {
            em.getTransaction().commit();

            String message = "Room cannot be found.";
            throw new RequestErrorException(message);
        }
        List<Message> messages;
        RoomSetting setting = client.getRoomSetting(room);
        Message lastMessage = setting.getLastMessage();
        if (lastMessage == null) {
            messages = em.createQuery("select m from Message m " +
                    "where m.createTime >= :time")
                .setParameter("time", setting.getEnterTime())
                .getResultList();
        } else {
            messages = em.createQuery("select m from Message m " +
                    "where m.id > :id")
                .setParameter("id", setting.getLastMessage().getId())
                .getResultList();
        }
        Message last = messages.get(messages.size() - 1);
        setting.setLastMessage(last);
        em.getTransaction().commit();
        return messages.toArray(new Message[messages.size()]);
    }

    private Client getClient(String username) {
        return (Client)em.find(Client.class, username);
    }

    private Room getRoom(String roomKey) {
        return (Room)em.find(Room.class, roomKey);
    }

}

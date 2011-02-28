package ca.wasabistudio.chat.rs;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.transaction.annotation.Transactional;

import ca.wasabistudio.chat.dto.ClientDTO;
import ca.wasabistudio.chat.dto.MessageDTO;
import ca.wasabistudio.chat.dto.RoomDTO;
import ca.wasabistudio.chat.entity.Client;
import ca.wasabistudio.chat.entity.Message;
import ca.wasabistudio.chat.entity.Room;
import ca.wasabistudio.chat.entity.RoomSetting;

@Path("/room")
public class RoomResource {

    private EntityManager em;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @GET
    @Path("/list")
    @Produces("application/json")
    @Transactional
    @SuppressWarnings("unchecked")
    public List<RoomDTO> getRooms() {
        List<Room> rooms = em.createQuery("select r from Room r")
            .getResultList();
        return RoomDTO.toDTOs(rooms);
    }

    @GET
    @Path("/{room}/clients")
    @Produces("application/json")
    @Transactional
    public Collection<ClientDTO> getRoomClients(@PathParam("room") String roomKey) {
        if ("".equals(roomKey)) {
            String message = "Room key cannot be empty.";
            throw new RequestErrorException(message);
        }
        Room room = getRoom(roomKey);
        if (room == null) {
            String message = "Room cannot be found.";
            throw new NotFoundException(message);
        }
        Set<Client> clients = room.getClients();
        return ClientDTO.toDTOs(clients);
    }

    @GET
    @Path("/join/{room}/{client}")
    @Produces("application/json")
    @Transactional
    public void joinRoom(@PathParam("room") String roomKey,
            @PathParam("client") String username) {
        if ("".equals(roomKey) || "".equals(username)) {
            String message = "Room key or username cannot be empty.";
            throw new RequestErrorException(message);
        }
        Client client = getClient(username);
        if (client == null) {
            String message = "Client cannot be found.";
            throw new RequestErrorException(message);
        }

        Room room = getRoom(roomKey);
        if (room == null) {
            room = new Room(roomKey);
        }

        room.addClient(client);
    }

    @GET
    @Path("/messages/{room}/{client}")
    @Produces("application/json")
    @Transactional
    @SuppressWarnings("unchecked")
    public MessageDTO[] getMessages(@PathParam("room") String roomKey,
            @PathParam("client") String username) {
        Client client = getClient(username);
        if (client == null) {
            String message = "Client cannot be found.";
            throw new RequestErrorException(message);
        }

        Room room = getRoom(roomKey);
        if (room == null) {
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
        if (messages.size() > 0) {
            Message last = messages.get(messages.size() - 1);
            setting.setLastMessage(last);
        }
        List<MessageDTO> result = MessageDTO.toDTOs(messages);
        return result.toArray(new MessageDTO[messages.size()]);
    }

    private Client getClient(String username) {
        return (Client)em.find(Client.class, username);
    }

    private Room getRoom(String roomKey) {
        return (Room)em.find(Room.class, roomKey);
    }

}

package ca.wasabistudio.chat.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Room {

    @Id
    @Access(AccessType.FIELD)
    private String key;

    private String title;
    private String motd;

    @Temporal(TemporalType.TIMESTAMP)
    @Access(AccessType.FIELD)
    private Date createTime;

    @OneToMany(cascade = CascadeType.ALL)
    @Access(AccessType.FIELD)
    private List<Message> messages;

    @ManyToMany
    @Access(AccessType.FIELD)
    @OrderBy("username")
    private Set<Client> clients;

    private Message lastMessage;

    Room() {
        this.key = "";
        this.title = "";
        this.motd = "";
        this.createTime = new Date();
        this.messages = new ArrayList<Message>();
        this.clients = new HashSet<Client>();
    }

    public Room(String key) {
        this();
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMotd() {
        return motd;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public Date getCreateTime() {
        return (Date)createTime.clone();
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public Set<Client> getClients() {
        return clients;
    }

    public void addClient(Client client) {
        RoomSetting setting = new RoomSetting(client, this);
        setting.setLastMessage(getLastMessage());
        client.addRoomSetting(setting);

        clients.add(client);
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message message) {
        lastMessage = message;
    }

    @Override
    public int hashCode() {
        return this.getKey().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Room) {
            Room otherRoom = (Room)other;
            if ("".equals(getKey()) || "".equals(otherRoom.getKey())) {
                return super.equals(other);
            }
            return getKey().equals(otherRoom.getKey());
        }
        return false;
    }

}

package ca.wasabistudio.chat.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class RoomSetting implements Serializable {

    private static final long serialVersionUID = 185641357189839497L;

    @Id
    @GeneratedValue
    @Access(AccessType.FIELD)
    private int id;

    private String status;

    private Message lastMessage;

    @ManyToOne
    @Access(AccessType.FIELD)
    private Client client;

    @ManyToOne
    @Access(AccessType.FIELD)
    private Room room;

    @Temporal(TemporalType.TIMESTAMP)
    @Access(AccessType.FIELD)
    private Date enterTime;

    RoomSetting() {
        this.id = 0;
        this.enterTime = new Date();
    }

    public RoomSetting(Client client, Room room) {
        this();
        this.client = client;
        this.room = room;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Date getEnterTime() {
        return enterTime;
    }

}

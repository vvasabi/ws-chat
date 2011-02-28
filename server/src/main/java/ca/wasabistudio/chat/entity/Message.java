package ca.wasabistudio.chat.entity;

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
public class Message {

    @Id
    @GeneratedValue
    @Access(AccessType.FIELD)
    private int id;

    private String body;

    @ManyToOne
    @Access(AccessType.FIELD)
    private Client client;

    @ManyToOne
    @Access(AccessType.FIELD)
    private Room room;

    @Temporal(TemporalType.TIMESTAMP)
    @Access(AccessType.FIELD)
    private Date createTime;

    Message() {
        id = 0;
        body = "";
        client = null;
        createTime = new Date();
    }

    public Message(Client client, Room room, String body) {
        this();
        this.client = client;
        this.room = room;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Client getClient() {
        return client;
    }

    public Room getRoom() {
        return room;
    }

    public Date getCreateTime() {
        return (Date)createTime.clone();
    }

}

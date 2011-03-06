package ca.wasabistudio.chat.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="messages")
public class Message implements Serializable {

    private static final long serialVersionUID = 3635563826579404945L;

    @Id
    @Column(name="message_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Access(AccessType.FIELD)
    private int id;

    @Column(name="body")
    private String body;

    @JoinColumn(name="username")
    @ManyToOne(optional=true)
    @Access(AccessType.FIELD)
    private Client client;

    @JoinColumn(name="room_key")
    @ManyToOne(optional=true)
    @Access(AccessType.FIELD)
    private Room room;

    @Column(name="create_time")
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

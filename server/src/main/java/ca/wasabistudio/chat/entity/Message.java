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

    @Column(name="body", length=300)
    private String body;

    @Column(name="username")
    @Access(AccessType.FIELD)
    private String username;

    @Column(name="room_key")
    @Access(AccessType.FIELD)
    private String roomKey;

    @Column(name="create_time")
    @Temporal(TemporalType.TIMESTAMP)
    @Access(AccessType.FIELD)
    private Date createTime;

    Message() {
        id = 0;
        body = "";
        username = "";
        createTime = new Date();
    }

    public Message(Client client, Room room, String body) {
        this();
        this.username = client.getUsername();
        this.roomKey = room.getKey();
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

    public String getUsername() {
        return username;
    }

    public String getRoomKey() {
        return roomKey;
    }

    public Date getCreateTime() {
        return (Date)createTime.clone();
    }

}

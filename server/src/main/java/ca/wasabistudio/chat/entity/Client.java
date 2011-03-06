package ca.wasabistudio.chat.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="clients")
public class Client implements Serializable {

    private static final long serialVersionUID = 7958127884926450063L;

    @Id
    @Column(name="username", length=100)
    @Access(AccessType.FIELD)
    private String username;

    @Column(name="status", length=10)
    private String status;

    @Column(name="session_id", length=32)
    private String sessionId;

    @Column(name="last_sync")
    @Temporal(TemporalType.TIMESTAMP)
    @Access(AccessType.FIELD)
    private Date lastSync;

    @OneToMany(cascade=CascadeType.PERSIST, mappedBy="client")
    @Access(AccessType.FIELD)
    private Set<Message> messages;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="client")
    @Access(AccessType.FIELD)
    private Set<RoomSetting> roomSettings;

    Client() {
        username = "";
        status = "";
        lastSync = new Date();
        messages = new HashSet<Message>();
        roomSettings = new HashSet<RoomSetting>();
    }

    public Client(String username) {
        this();
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getLastSync() {
        return (Date)lastSync.clone();
    }

    public void sync() {
        lastSync = new Date();
    }

    public Set<Message> getMessages() {
        return Collections.unmodifiableSet(messages);
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void removeMessage(Message message) {
        messages.remove(message);
    }

    public RoomSetting getRoomSetting(Room room) {
        for (RoomSetting setting : roomSettings) {
            if (setting.getRoom().equals(room)) {
                return setting;
            }
        }
        return null;
    }

    void addRoomSetting(RoomSetting setting) {
        roomSettings.add(setting);
    }

    void removeRoomSetting(Room room) {
        RoomSetting setting = getRoomSetting(room);
        if (setting != null) {
            roomSettings.remove(setting);
        }
    }

    @Override
    public int hashCode() {
        return this.getUsername().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Client) {
            Client otherClient = (Client)other;
            if ("".equals(getUsername()) ||
                    "".equals(otherClient.getUsername())) {
                return super.equals(other);
            }
            return getUsername().equals(otherClient.getUsername());
        }
        return false;
    }

}

package ca.wasabistudio.chat.entity;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Client {

    @Id
    @Access(AccessType.FIELD)
    private String username;

    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @Access(AccessType.FIELD)
    private Date lastSync;

    @OneToMany(cascade = CascadeType.ALL)
    @Access(AccessType.FIELD)
    private Set<Message> messages;

    @OneToMany
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

}

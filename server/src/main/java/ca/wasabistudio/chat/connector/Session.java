package ca.wasabistudio.chat.connector;

import java.util.Calendar;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="phpbb_sessions")
class Session {

    private static final long THOUSAND = 1000;

    @Id
    @Column(name="session_id")
    private String id;

    @Column(name="session_user_id")
    private int userId;

    @Access(AccessType.FIELD)
    @Column(name="session_last_visit")
    private long lastUpdate;

    @Column(name="session_ip")
    private String ip;

    Session() {
        id = "";
        userId = 0;
        lastUpdate = 0;
        ip = "";
    }

    Session(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Calendar getLastUpdate() {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(toMiliSeconds(lastUpdate));
        return instance;
    }

    public void setLastUpdate(Calendar lastUpdate) {
        this.lastUpdate = toSeconds(lastUpdate.getTimeInMillis());
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    private static long toMiliSeconds(long seconds) {
        return seconds * THOUSAND;
    }

    private static long toSeconds(long miliSeconds) {
        return miliSeconds / THOUSAND;
    }

}

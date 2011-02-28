package ca.wasabistudio.ca.chat.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import ca.wasabistudio.chat.entity.Room;

public class RoomDTO {

    private String key;

    private String title;

    private String motd;

    private Date createTime;

    public RoomDTO() {
        this.key = "";
        this.title = "";
        this.motd = "";
        this.createTime = new Date();
    }

    public RoomDTO(Room room) {
        this.key = room.getKey();
        this.title = room.getTitle();
        this.motd = room.getMotd();
        this.createTime = room.getCreateTime();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
        return (createTime == null) ? null : (Date)createTime.clone();
    }

    public void setCreateTime(Date createTime) {
        this.createTime = (createTime == null) ? null : (Date)createTime.clone();
    }

    public static Collection<RoomDTO> toDTOs(Collection<Room> rooms) {
        Collection<RoomDTO> result = new HashSet<RoomDTO>();
        for (Room room : rooms) {
            result.add(new RoomDTO(room));
        }
        return result;
    }

    public static List<RoomDTO> toDTOs(List<Room> rooms) {
        List<RoomDTO> result = new ArrayList<RoomDTO>(rooms.size());
        for (Room room : rooms) {
            result.add(new RoomDTO(room));
        }
        return result;
    }

}

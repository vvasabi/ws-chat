package ca.wasabistudio.ca.chat.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import ca.wasabistudio.chat.entity.Client;

@SuppressWarnings("serial")
public class ClientDTO implements Serializable {

    private String username;
    private String status;
    private Date lastSync;
    private Date enterTime;

    public ClientDTO() {
        username = "";
        status = "";
    }

    public ClientDTO(Client client) {
        username = client.getUsername();
        status = client.getStatus();
        lastSync = client.getLastSync();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastSync() {
        return (lastSync == null) ? null : (Date)lastSync.clone();
    }

    public void setLastSync(Date lastSync) {
        this.lastSync = (lastSync == null) ? null : (Date)lastSync.clone();
    }

    public Date getEnterTime() {
        return (enterTime == null) ? null : (Date)enterTime.clone();
    }

    public void setEnterTime(Date enterTime) {
        this.enterTime = (enterTime == null) ? null : (Date)enterTime.clone();
    }

    public static Collection<ClientDTO> toDTOs(Collection<Client> clients) {
        Collection<ClientDTO> result = new HashSet<ClientDTO>();
        for (Client client : clients) {
            result.add(new ClientDTO(client));
        }
        return result;
    }

    public static List<ClientDTO> toDTOs(List<Client> clients) {
        List<ClientDTO> result = new ArrayList<ClientDTO>(clients.size());
        for (Client client : clients) {
            result.add(new ClientDTO(client));
        }
        return result;
    }

}

package ca.wasabistudio.ca.dto;

import java.io.Serializable;
import java.util.Date;

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
		enterTime = client.getEnterTime();
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

}

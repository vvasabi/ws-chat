package ca.wasabistudio.chat.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

@Entity
@Table(name="messages")
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE,
	getterVisibility=JsonAutoDetect.Visibility.NONE)
public class Message implements Serializable {

	public static enum Type {
		Regular, Entrance, Exit
	}

	private static final long serialVersionUID = 3635563826579404945L;

	@Id
	@Column(name="message_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Access(AccessType.FIELD)
	private Integer id;

	@Column(name="type", length=10)
	@Enumerated(EnumType.STRING)
	@JsonProperty
	private Type type;

	@Column(name="body", length=300)
	@JsonProperty
	private String body;

	@Column(name="username")
	@Access(AccessType.FIELD)
	@JsonProperty("client")
	private String username;

	@Column(name="room_key")
	@Access(AccessType.FIELD)
	@JsonProperty("room")
	private String roomKey;

	@Column(name="create_time")
	@Temporal(TemporalType.TIMESTAMP)
	@Access(AccessType.FIELD)
	@JsonProperty
	private Date createTime;

	Message() {
		id = 0;
		type = Type.Regular;
		body = "";
		username = "";
		createTime = new Date();
	}

	public Message(Client client, Room room, Type type) {
		this();
		this.username = client.getUsername();
		this.roomKey = room.getKey();
		this.type = type;
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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
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

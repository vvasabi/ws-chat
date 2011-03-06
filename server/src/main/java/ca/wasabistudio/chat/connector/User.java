package ca.wasabistudio.chat.connector;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="phpbb_users")
class User implements Serializable {

    private static final long serialVersionUID = -8704686638997360442L;

    @Id
    @GeneratedValue
    @Column(name="user_id")
    @Access(AccessType.FIELD)
    private int id;

    @Column(name="username")
    private String username;

    User() {
        id = 0;
        username = "";
    }

    User(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

}

package ca.wasabistudio.chat.support;

import java.io.Serializable;

import ca.wasabistudio.chat.entity.Client;

public class Session implements Serializable {

    private static final long serialVersionUID = 5974606251362198260L;

    private Client client;

    public synchronized Client getClient() {
        return client;
    }

    public synchronized void setClient(Client client) {
        this.client = client;
    }

}

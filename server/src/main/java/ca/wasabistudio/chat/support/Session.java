package ca.wasabistudio.chat.support;

import ca.wasabistudio.chat.entity.Client;

public class Session {

    private Client client;

    public synchronized Client getClient() {
        return client;
    }

    public synchronized void setClient(Client client) {
        this.client = client;
    }

}

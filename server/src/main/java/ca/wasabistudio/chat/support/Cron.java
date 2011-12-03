package ca.wasabistudio.chat.support;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import ca.wasabistudio.chat.connector.Connector;
import ca.wasabistudio.chat.entity.Client;
import ca.wasabistudio.chat.entity.Room;

public class Cron {

	// 90 seconds
	private static final long TIMEOUT = 90000;

	private EntityManager em;
	private Connector connector;

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	public void setConnector(Connector connector) {
		this.connector = connector;
	}

	public void run() {
		removeTimeoutUsers();
		refreshSessions();
	}

	@Transactional
	@SuppressWarnings("unchecked")
	private void removeTimeoutUsers() {
		Calendar now = Calendar.getInstance();
		Date expired = new Date(now.getTimeInMillis() - TIMEOUT);
		String query = "select c from Client c where c.lastSync < :time";
		List<Client> clients = em.createQuery(query)
			.setParameter("time", expired)
			.getResultList();
		List<Room> rooms = em.createQuery("select r from Room r")
			.getResultList();

		for (Client client : clients) {
			for (Room room : rooms) {
				room.removeClient(client);
			}
			removeClient(client);
		}
	}

	@Transactional
	@SuppressWarnings("unchecked")
	private void refreshSessions() {
		List<Client> clients = em.createQuery("select c from Client c")
			.getResultList();
		for (Client client : clients) {
			if (!connector.refreshSession(client.getSessionId())) {
				System.out.println("Can't refresh session!");
				removeClient(client);
			}
		}
	}

	@Transactional
	private void removeClient(Client client) {
		client.exitAllRooms();
		System.out.println("Removing client...");
		em.remove(client);
	}

}

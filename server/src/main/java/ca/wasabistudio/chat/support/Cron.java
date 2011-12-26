package ca.wasabistudio.chat.support;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.wasabistudio.chat.connector.Connector;
import ca.wasabistudio.chat.entity.Client;
import ca.wasabistudio.chat.entity.Message;
import ca.wasabistudio.chat.entity.Room;
import ca.wasabistudio.chat.entity.Message.Type;
import ca.wasabistudio.chat.repo.ClientRepository;
import ca.wasabistudio.chat.repo.RoomRepository;

public class Cron {

	// 90 seconds
	private static final long TIMEOUT = 90000;

	@Autowired
	private RoomRepository roomRepo;

	@Autowired
	private ClientRepository clientRepo;

	private Map<String, UpdateQueue> messageUpdateQueues;

	private Connector connector;

	public void setConnector(Connector connector) {
		this.connector = connector;
	}

	public void setMessageUpdateQueues(Map<String, UpdateQueue> queues) {
		messageUpdateQueues = queues;
	}

	public void run() {
		List<Room> roomToNotify = removeTimeoutUsers();
		for (Room room : roomToNotify) {
			UpdateQueue queue = messageUpdateQueues.get(room.getKey());
			if (queue == null) {
				continue;
			}

			queue.pushUpdate(null);
		}

		refreshSessions();
	}

	/**
	 * Remove users timed out.
	 *
	 * @return a list of rooms with clients removed
	 */
	@Transactional
	private List<Room> removeTimeoutUsers() {
		Calendar now = Calendar.getInstance();
		Date expiration = new Date(now.getTimeInMillis() - TIMEOUT);
		List<Client> clients = clientRepo.findExpiredClients(expiration);
		List<Room> rooms = roomRepo.findAll();
		List<Room> roomToNotify = new ArrayList<Room>();
		for (Client client : clients) {
			for (Room room : rooms) {
				if (!room.hasClient(client)) {
					continue;
				}
				room.removeClient(client);

				// create a message
				Message message = new Message(client, room, Type.Exit);
				room.addMessage(message);

				if (!roomToNotify.contains(room)) {
					roomToNotify.add(room);
				}
			}
			removeClient(client);
		}
		return roomToNotify;
	}

	@Transactional
	private void refreshSessions() {
		List<Client> clients = clientRepo.findAll();
		for (Client client : clients) {
			if (!connector.refreshSession(client.getSessionId())) {
				removeClient(client);
			}
		}
	}

	@Transactional
	private void removeClient(Client client) {
		client.exitAllRooms();
		clientRepo.delete(client);
	}

}

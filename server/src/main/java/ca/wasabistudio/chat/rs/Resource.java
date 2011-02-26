package ca.wasabistudio.chat.rs;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class Resource {

	private static final String PERSISTENCE_UNIT = "chat";

	private static EntityManagerFactory emf;

	public static EntityManagerFactory getEntityManagerFactory() {
		if (emf == null) {
			emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
		}
		return emf;
	}

}

package ca.wasabistudio.chat.rs;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class ChatApplication extends Application {

	private static final Set<Class<?>> classes;

	static {
		classes = new HashSet<Class<?>>();
		classes.add(ClientResource.class);
	}

	public Set<Class<?>> getClasses() {
		return classes;
	}

}

package ca.wasabistudio.chat.rs;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

@SuppressWarnings("deprecation")
public class MockHttpSession implements HttpSession {

	private final String id;

	public MockHttpSession(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Object getAttribute(String key) {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public long getCreationTime() {
		// NOT IMPLEMENTED
		return 0;
	}

	@Override
	public long getLastAccessedTime() {
		// NOT IMPLEMENTED
		return 0;
	}

	@Override
	public int getMaxInactiveInterval() {
		// NOT IMPLEMENTED
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public HttpSessionContext getSessionContext() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public Object getValue(String key) {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public String[] getValueNames() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public void invalidate() {
		// NOT IMPLEMENTED

	}

	@Override
	public boolean isNew() {
		// NOT IMPLEMENTED
		return false;
	}

	@Override
	public void putValue(String key, Object value) {
		// NOT IMPLEMENTED
	}

	@Override
	public void removeAttribute(String key) {
		// NOT IMPLEMENTED
	}

	@Override
	public void removeValue(String key) {
		// NOT IMPLEMENTED
	}

	@Override
	public void setAttribute(String key, Object value) {
		// NOT IMPLEMENTED
	}

	@Override
	public void setMaxInactiveInterval(int max) {
		// NOT IMPLEMENTED
	}

}

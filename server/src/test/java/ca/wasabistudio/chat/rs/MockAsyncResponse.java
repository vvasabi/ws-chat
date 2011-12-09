package ca.wasabistudio.chat.rs;

import javax.ws.rs.core.Response;

import org.jboss.resteasy.spi.AsynchronousResponse;

public class MockAsyncResponse implements AsynchronousResponse {

	private boolean hasResponse;
	private Response response;

	@Override
	public synchronized void setResponse(Response response) {
		if (!hasResponse) {
			this.response = response;
			hasResponse = true;
		}
	}

	public synchronized boolean isResponseSet() {
		return hasResponse;
	}

	public synchronized Response getResponse() {
		return response;
	}

}

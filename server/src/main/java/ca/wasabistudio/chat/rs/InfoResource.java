package ca.wasabistudio.chat.rs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Info service that provides generic API information.
 *
 * @author wasabi
 */
@Path("/info")
public class InfoResource {

	private static final int API_VERSION = 1;

	@GET
	@Path("/api")
	@Produces("text/plain")
	public int getApiVersion() {
		return API_VERSION;
	}

}

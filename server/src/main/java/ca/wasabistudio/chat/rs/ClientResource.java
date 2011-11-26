package ca.wasabistudio.chat.rs;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.springframework.transaction.annotation.Transactional;

import ca.wasabistudio.chat.connector.Connector;
import ca.wasabistudio.chat.entity.Client;
import ca.wasabistudio.chat.support.AlreadyJoinedException;
import ca.wasabistudio.chat.support.RequestErrorException;
import ca.wasabistudio.chat.support.Session;
import ca.wasabistudio.chat.support.SessionException;

/**
 * REST services for working with users of the chat system.
 *
 * @author wasabi
 */
@Path("/client")
public class ClientResource {

    private EntityManager em;
    private Connector connector;
    private Session session;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * Get the current session id.
     *
     * @param request current HttpServletRequest instance
     * @return current session id
     */
    @GET
    @Path("/session")
    @Produces("application/json")
    public String getSession(@Context HttpServletRequest request) {
        return request.getSession().getId();
    }

    /**
     * Allow a user to enter a room.
     *
     * @param sessionId forums sessionId of the user
     * @param config current ServletConfig instance
     * @param request current HttpServletRequest instance
     * @return forums username of the user
     */
    @POST
    @Path("/enter/{sessionId}")
    @Produces("text/plain")
    @Transactional
    public String enter(@PathParam("sessionId") String sessionId,
            @Context ServletConfig config,
            @Context HttpServletRequest request) {
        if ((sessionId == null) || "".equals(sessionId)) {
            String message = "Session id cannot be empty.";
            throw new RequestErrorException(message);
        }

        String username = sessionId;
        if (isProductionMode(config)) {
            if (!connector.validateSession(sessionId, getIp(request))) {
                throw new SessionException("Unable to enter.");
            }

            username = connector.getUsername(sessionId);
        }

        if (em.find(Client.class, username) != null) {
            throw new AlreadyJoinedException();
        }

        Client client = new Client(username);
        client.setSessionId(sessionId);
        em.persist(client);
        session.setClient(client);
        return username;
    }

    private String getIp(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    private boolean isProductionMode(ServletConfig config) {
        if (config == null) {
            return false;
        }
        String mode = config.getServletContext().getInitParameter("mode");
        return "PRODUCTION".equals(mode);
    }

}

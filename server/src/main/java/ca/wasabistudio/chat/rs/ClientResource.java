package ca.wasabistudio.chat.rs;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.transaction.annotation.Transactional;

import ca.wasabistudio.chat.dto.ClientDTO;
import ca.wasabistudio.chat.entity.Client;
import ca.wasabistudio.chat.support.AlreadyJoinedException;
import ca.wasabistudio.chat.support.RequestErrorException;
import ca.wasabistudio.chat.support.Session;

@Path("/client")
public class ClientResource {

    private EntityManager em;
    private Session session;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @POST
    @Path("/join")
    @Produces("application/json")
    @Transactional
    public ClientDTO addClient(ClientDTO dto) {
        if ("".equals(dto.getUsername())) {
            String message = "Username cannot be empty.";
            throw new RequestErrorException(message);
        }
        if (em.find(Client.class, dto.getUsername()) != null) {
            throw new AlreadyJoinedException();
        }

        Client client = new Client(dto.getUsername());
        client.setStatus(dto.getStatus());
        em.persist(client);
        session.setClient(client);
        return dto;
    }

}

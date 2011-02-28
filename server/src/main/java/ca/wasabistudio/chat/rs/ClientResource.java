package ca.wasabistudio.chat.rs;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.transaction.annotation.Transactional;

import ca.wasabistudio.chat.dto.ClientDTO;
import ca.wasabistudio.chat.entity.Client;

@Path("/client")
public class ClientResource {

    private EntityManager em;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @GET
    @Path("/list")
    @Produces("application/json")
    @SuppressWarnings("unchecked")
    @Transactional
    public List<ClientDTO> getClients() {
        List<Client> clients = em.createQuery("select c from Client c")
            .getResultList();
        return ClientDTO.toDTOs(clients);
    }

    @POST
    @Path("/add")
    @Produces("application/json")
    @Transactional
    public ClientDTO addClient(ClientDTO dto) {
        Client client = new Client(dto.getUsername());
        client.setStatus(dto.getStatus());
        em.persist(client);
        return dto;
    }

}

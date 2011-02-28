package ca.wasabistudio.chat.rs;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ca.wasabistudio.ca.chat.dto.ClientDTO;
import ca.wasabistudio.chat.entity.Client;

@Path("/client")
public class ClientResource {

    private EntityManager em;

    public void setEntityManagerFactory(EntityManagerFactory emf) {
        em = emf.createEntityManager();
    }

    public void destroy() {
        em.close();
    }

    @GET
    @Path("/list")
    @Produces("application/json")
    @SuppressWarnings("unchecked")
    public List<ClientDTO> getClients() {
        em.getTransaction().begin();
        List<Client> clients = em.createQuery("select c from Client c")
            .getResultList();
        em.getTransaction().commit();
        return ClientDTO.toDTOs(clients);
    }

    @POST
    @Path("/add")
    @Produces("application/json")
    public ClientDTO addClient(ClientDTO dto) {
        em.getTransaction().begin();
        Client client = new Client(dto.getUsername());
        client.setStatus(dto.getStatus());
        em.persist(client);
        em.getTransaction().commit();
        return dto;
    }

}

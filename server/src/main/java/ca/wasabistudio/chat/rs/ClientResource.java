package ca.wasabistudio.chat.rs;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ca.wasabistudio.ca.dto.ClientDTO;
import ca.wasabistudio.chat.entity.Client;

@Path("/client")
public class ClientResource extends Resource {

	@GET
	@Path("/list")
	@Produces("application/json")
	@SuppressWarnings("unchecked")
	public List<ClientDTO> getClients() {
		List<ClientDTO> result = new ArrayList<ClientDTO>();
		EntityManagerFactory emf = getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		List<Client> clients = em.createQuery("SELECT c from Client c")
			.getResultList();
		for (Client client : clients) {
			result.add(new ClientDTO(client));
		}
		em.getTransaction().commit();
		em.close();
		return result;
	}

	@POST
	@Path("/add")
	@Produces("application/json")
	public ClientDTO addClient(ClientDTO dto) {
		EntityManagerFactory emf = getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Client client = new Client(dto.getUsername());
		client.setStatus(dto.getStatus());
		em.persist(client);
		em.getTransaction().commit();
		em.close();
		return dto;
	}

}

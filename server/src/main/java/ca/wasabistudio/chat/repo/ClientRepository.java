package ca.wasabistudio.chat.repo;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import ca.wasabistudio.chat.entity.Client;

public interface ClientRepository
	extends PagingAndSortingRepository<Client, String> {

	@Override
	List<Client> findAll();

}

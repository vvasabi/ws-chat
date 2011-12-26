package ca.wasabistudio.chat.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import ca.wasabistudio.chat.entity.Client;

public interface ClientRepository
	extends PagingAndSortingRepository<Client, String> {

	@Override
	@Transactional(readOnly=true)
	List<Client> findAll();

	@Query(
		"select c from Client c "
			+ "where c.lastSync < :expiration"
	)
	@Transactional(readOnly=true)
	List<Client> findExpiredClients(@Param("expiration") Date expiration);

}

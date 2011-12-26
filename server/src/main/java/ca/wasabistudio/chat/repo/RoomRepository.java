package ca.wasabistudio.chat.repo;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import ca.wasabistudio.chat.entity.Room;

public interface RoomRepository
	extends PagingAndSortingRepository<Room, String> {

	@Override
	@Transactional(readOnly=true)
	List<Room> findAll();

}

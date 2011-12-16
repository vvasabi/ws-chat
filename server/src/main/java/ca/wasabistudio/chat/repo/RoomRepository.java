package ca.wasabistudio.chat.repo;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import ca.wasabistudio.chat.entity.Room;

public interface RoomRepository
	extends PagingAndSortingRepository<Room, String> {

	@Override
	List<Room> findAll();

}

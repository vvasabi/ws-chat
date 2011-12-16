package ca.wasabistudio.chat.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import ca.wasabistudio.chat.entity.Message;

public interface MessageRepository
	extends PagingAndSortingRepository<Message, Integer> {

	@Query(
		"select m from Message m " +
			"where m.createTime >= :time " +
				"and m.roomKey = :roomKey " +
				"and m.username <> :username " +
			"order by m.id"
	)
	List<Message> findMessagesByTime(
		@Param("time") Date time,
		@Param("roomKey") String roomKey,
		@Param("username") String username
	);

	@Query(
		"select m from Message m " +
			"where m.id > :id " +
				"and m.roomKey = :roomKey " +
				"and m.username <> :username " +
			"order by m.id"
	)
	List<Message> findMessagesByLastMessage(
		@Param("id") int id,
		@Param("roomKey") String roomKey,
		@Param("username") String username
	);

}

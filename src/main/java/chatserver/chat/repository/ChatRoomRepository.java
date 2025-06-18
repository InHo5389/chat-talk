package chatserver.chat.repository;

import chatserver.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT cr FROM ChatRoom cr " +
            "LEFT JOIN FETCH cr.chatParticipants cp " +
            "LEFT JOIN FETCH cp.member " +
            "WHERE cr.id = :roomId")
    Optional<ChatRoom> findByIdWithParticipants(@Param("roomId") Long roomId);

    List<ChatRoom> findByIsGroupChat(Boolean isGroupChat);
}

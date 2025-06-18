package chatserver.chat.repository;

import chatserver.chat.domain.ChatParticipant;
import chatserver.chat.domain.ChatRoom;
import chatserver.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
    Optional<ChatParticipant> findByChatRoomAndMember(ChatRoom chatRoom, Member member);
}

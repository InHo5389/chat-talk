package chatserver.chat.repository;

import chatserver.chat.domain.ChatRoom;
import chatserver.chat.domain.ReadStatus;
import chatserver.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, Long> {
    List<ReadStatus> findByChatRoomAndMemberAndIsReadFalse(ChatRoom chatRoom, Member member);
    Long countByChatRoomAndMemberAndIsReadFalse(ChatRoom chatRoom, Member member);
}

package chatserver.chat.repository;

import chatserver.chat.domain.ChatParticipant;
import chatserver.chat.domain.ChatRoom;
import chatserver.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
    Optional<ChatParticipant> findByChatRoomAndMember(ChatRoom chatRoom, Member member);

    List<ChatParticipant> findAllByMember(Member member);

    List<ChatParticipant> findByChatRoom(ChatRoom chatRoom);

    @Query("SELECT cp1.chatRoom FROM ChatParticipant cp1 JOIN ChatParticipant cp2 ON cp1.chatRoom.id = cp2.chatRoom.id " +
            "WHERE cp1.member.id = :myId AND cp2.member.id = :otherMemberId " +
            "AND cp1.chatRoom.isGroupChat=false")
    Optional<ChatRoom> findExistPrivateRoom(@Param("myId") Long myId,@Param("otherMemberId") Long otherMemberId);

}

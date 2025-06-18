package chatserver.chat.service;

import chatserver.chat.domain.ChatMessage;
import chatserver.chat.domain.ChatParticipant;
import chatserver.chat.domain.ChatRoom;
import chatserver.chat.domain.ReadStatus;
import chatserver.chat.dto.ChatMessageDto;
import chatserver.chat.dto.ChatRoomListDto;
import chatserver.chat.dto.MyChatListDto;
import chatserver.chat.repository.ChatMessageRepository;
import chatserver.chat.repository.ChatParticipantRepository;
import chatserver.chat.repository.ChatRoomRepository;
import chatserver.chat.repository.ReadStatusRepository;
import chatserver.member.domain.Member;
import chatserver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ChatParticipantRepository chatParticipantRepository;

    @Transactional
    public void saveMessage(Long roomId, ChatMessageDto chatMessageDto) {
        // 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new RuntimeException("채팅방이 존재하지 않습니다."));

        // 보낸사람 조회
        Member sender = memberRepository.findByEmail(chatMessageDto.getSenderEmail()).orElseThrow(
                () -> new RuntimeException("사용자가 존재하지 않습니다."));

        // 메시지 저장
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .member(sender)
                .content(chatMessageDto.getMessage())
                .build();
        chatMessageRepository.save(chatMessage);

        // 사용자 별 읽음 여부 저장
        List<ReadStatus> readStatuses = chatRoom.getChatParticipants().stream()
                .map(participant -> ReadStatus.builder()
                        .chatRoom(participant.getChatRoom())
                        .member(participant.getMember())
                        .chatMessage(chatMessage)
                        .isRead(participant.getMember().equals(sender))
                        .build())
                .toList();
        readStatusRepository.saveAll(readStatuses);
    }

    @Transactional
    public void createGroupRoom(String roomName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("회원이 존재하지 않습니다."));

        ChatRoom chatRoom = ChatRoom.builder()
                .name(roomName)
                .isGroupChat(true)
                .build();
        chatRoomRepository.save(chatRoom);

        ChatParticipant chatParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .member(member)
                .build();
        chatParticipantRepository.save(chatParticipant);
    }

    public List<ChatRoomListDto> getGroupChatRooms() {
        return chatRoomRepository.findByIsGroupChat(true).stream()
                .map(ChatRoomListDto::fromEntity)
                .toList();
    }

    public void addParticipantToGroupChat(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new RuntimeException("채팅방이 존재하지 않습니다."));

        // 큰 필터링 통과하고 나면 authentication 객체 안에 들어가 있음
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("회원이 존재하지 않습니다."));

        Optional<ChatParticipant> optionalChatParticipant = chatParticipantRepository.findByChatRoomAndMember(chatRoom, member);
        if (optionalChatParticipant.isEmpty()) {
            addParticipantToRoom(chatRoom, member);
        }
    }

    public void addParticipantToRoom(ChatRoom chatRoom, Member member) {
        ChatParticipant chatParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .member(member)
                .build();
        chatParticipantRepository.save(chatParticipant);
    }

    public List<ChatMessageDto> getChatHistory(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new RuntimeException("채팅방이 존재하지 않습니다."));

        // 큰 필터링 통과하고 나면 authentication 객체 안에 들어가 있음
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("회원이 존재하지 않습니다."));

        chatRoom.getChatParticipants().stream()
                .filter(participant -> participant.getMember().equals(member))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("채팅방에 참여하지 않은 사용자입니다."));

        return chatRoom.getChatMessages().stream()
                .sorted(Comparator.comparing(ChatMessage::getCreatedTime))
                .map(ChatMessageDto::fromEntity)
                .toList();
    }

    public boolean isRoomParticipant(String email, Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new RuntimeException("채팅방이 존재하지 않습니다."));

        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("회원이 존재하지 않습니다."));

        return chatRoom.getChatParticipants().stream()
                .anyMatch(participant -> participant.equals(member));
    }

    @Transactional
    public void messageRead(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new RuntimeException("채팅방이 존재하지 않습니다."));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("회원이 존재하지 않습니다."));

        List<ReadStatus> readStatuses = readStatusRepository.findByChatRoomAndMemberAndIsReadFalse(chatRoom, member);
        readStatuses.forEach(readStatus -> readStatus.updateIsRead(true));
    }

    @Transactional(readOnly = true)
    public List<MyChatListDto> getMyChatRooms() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("회원이 존재하지 않습니다."));

        List<ChatParticipant> chatParticipants = chatParticipantRepository.findAllByMember(member);
        List<MyChatListDto> chatListDtos = new ArrayList<>();
        for (ChatParticipant participant : chatParticipants) {
            Long count = readStatusRepository.countByChatRoomAndMemberAndIsReadFalse(participant.getChatRoom(), member);
            MyChatListDto myChatListDto = MyChatListDto.builder()
                    .roomId(participant.getChatRoom().getId())
                    .roomName(participant.getChatRoom().getName())
                    .isGroupChat(participant.getChatRoom().getIsGroupChat().toString())
                    .unReadCount(count)
                    .build();

            chatListDtos.add(myChatListDto);
        }

        return chatListDtos;
    }

    // 참여자 객체 삭제

    /**
     * 1. 참여자 객체만 삭제
     * 2. 모두가 나간 경우 읽음 여부 등 삭제
     */
    @Transactional
    public void leaveGroupChatRoom(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new RuntimeException("채팅방이 존재하지 않습니다."));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("회원이 존재하지 않습니다."));

        if (!chatRoom.getIsGroupChat()) {
            throw new RuntimeException("단체 채팅방이 아닙니다.");
        }

        ChatParticipant participant = chatParticipantRepository.findByChatRoomAndMember(chatRoom, member).orElseThrow(
                () -> new RuntimeException("회원이 존재하지 않습니다."));
        chatParticipantRepository.delete(participant);

        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        if(chatParticipants.isEmpty()){
            chatRoomRepository.delete(chatRoom);
        }
    }
}

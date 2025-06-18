package chatserver.chat.controller;

import chatserver.chat.dto.MyChatListDto;
import chatserver.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    // 그룹 채팅방 개설
    @PostMapping("/room/group/create")
    public ResponseEntity<?> createGroupRoom(@RequestParam String roomName) {
        chatService.createGroupRoom(roomName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/room/group/list")
    public ResponseEntity<?> getGroupChatRooms() {
        return ResponseEntity.ok(chatService.getGroupChatRooms());
    }

    @PostMapping("/room/group/{roomId}/join")
    public ResponseEntity<?> joinGroupChatRoom(@PathVariable Long roomId) {
        chatService.addParticipantToGroupChat(roomId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/history/{roomId}")
    public ResponseEntity<?> getChatHistory(@PathVariable Long roomId) {
        return ResponseEntity.ok().body(chatService.getChatHistory(roomId));
    }

    @PostMapping("/room/{roomId}/read")
    public ResponseEntity<?> messageRead(@PathVariable Long roomId) {
        chatService.messageRead(roomId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my/rooms")
    public ResponseEntity<?> getMyChatRooms() {
        return ResponseEntity.ok().body(chatService.getMyChatRooms());
    }

    @DeleteMapping("/room/group/{roomId}/leave")
    public ResponseEntity<?> leaveGroupChatRoom(@PathVariable Long roomId){
        chatService.leaveGroupChatRoom(roomId);
        return ResponseEntity.ok().build();
    }
}

package chatserver.chat.dto;

import chatserver.chat.domain.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomListDto {
    private Long roomId;
    private String roomName;

    public static ChatRoomListDto fromEntity(ChatRoom chatRoom) {
        return new ChatRoomListDto(
                chatRoom.getId(),
                chatRoom.getName()
        );
    }
}

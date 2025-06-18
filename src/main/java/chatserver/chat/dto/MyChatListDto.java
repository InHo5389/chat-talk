package chatserver.chat.dto;

import chatserver.chat.domain.ChatParticipant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyChatListDto {

    private Long roomId;
    private String roomName;
    private String isGroupChat;
    private Long unReadCount;

    public static MyChatListDto fromEntity(ChatParticipant chatParticipant,Long unReadCount) {
        return MyChatListDto.builder()
                .roomId(chatParticipant.getChatRoom().getId())
                .roomName(chatParticipant.getChatRoom().getName())
                .isGroupChat(chatParticipant.getChatRoom().getIsGroupChat().toString())
                .unReadCount(unReadCount)
                .build();
    }
}

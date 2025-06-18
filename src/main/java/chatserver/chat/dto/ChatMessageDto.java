package chatserver.chat.dto;

import chatserver.chat.domain.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

    private String senderEmail;
    private String message;

    public static ChatMessageDto fromEntity(ChatMessage chatMessage) {
        return new ChatMessageDto(chatMessage.getMember().getEmail(), chatMessage.getContent());
    }
}

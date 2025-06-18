package chatserver.chat.dto;

import chatserver.chat.domain.ChatMessage;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

    private Long roomId;
    private String senderEmail;
    private String message;

    public static ChatMessageDto fromEntity(ChatMessage chatMessage) {
        return ChatMessageDto.builder()
                .senderEmail(chatMessage.getMember().getEmail())
                .message(chatMessage.getContent())
                .build();
    }
}

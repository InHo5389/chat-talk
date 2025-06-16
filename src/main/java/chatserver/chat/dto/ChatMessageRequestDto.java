package chatserver.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequestDto {

    private String senderEmail;
    private String message;
}

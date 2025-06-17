package chatserver.chat.controller;

import chatserver.chat.dto.ChatMessageRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StompController {

    private final SimpMessageSendingOperations messageTemplate;

    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMessageRequestDto chatMessageRequestDto) {
        log.info("StompController sendMessage roomId: {}, message: {}", roomId, chatMessageRequestDto);
        // @SendTo 어노테이션
        messageTemplate.convertAndSend("/topic/" + roomId, chatMessageRequestDto);
    }
}

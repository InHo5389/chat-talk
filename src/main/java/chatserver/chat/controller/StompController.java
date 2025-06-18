package chatserver.chat.controller;

import chatserver.chat.dto.ChatMessageDto;
import chatserver.chat.service.ChatService;
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

    private final ChatService chatService;
    private final SimpMessageSendingOperations messageTemplate;

    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMessageDto chatMessageDto) {
        log.info("StompController sendMessage roomId: {}, message: {}", roomId, chatMessageDto);
        chatService.saveMessage(roomId, chatMessageDto);

        // @SendTo 어노테이션
        messageTemplate.convertAndSend("/topic/" + roomId, chatMessageDto);

    }
}

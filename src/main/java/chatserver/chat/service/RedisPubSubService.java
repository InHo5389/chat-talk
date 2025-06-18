package chatserver.chat.service;

import chatserver.chat.dto.ChatMessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

/**
 * 구독하기 위한 MessageListener 구현
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisPubSubService implements MessageListener {

    private final StringRedisTemplate stringRedisTemplate;
    private final SimpMessageSendingOperations messageTemplate;

    /**
     * pattern에는 topic의 이름의 패턴이 담겨있고, 이 패턴을 기반으로 동적으로 코딩 가능
     * 우리는 패턴이 따로 없고 chat 이라고 이렇게 담겨져 있음
     * <p>
     * 서버1이 redis로 메시지를 보내면 redis가 서버1, 서버2한테 메시지를 줄것이고(onMessage - Message)
     * 이걸 서버1,서버2에 있는 room1,room2에 넣어줘야함.
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String receivedMessage = message.toString();
        log.info("Received raw payload: {}", receivedMessage);
        log.info("Received raw receivedMessage: {}", receivedMessage);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ChatMessageDto chatMessageDto = objectMapper.readValue(receivedMessage, ChatMessageDto.class);
            messageTemplate.convertAndSend("/topic/" + chatMessageDto.getRoomId(), chatMessageDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void publish(String channel, String message) {
        stringRedisTemplate.convertAndSend(channel, message);
    }
}

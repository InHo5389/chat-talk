package chatserver.chat.handler;

import chatserver.chat.service.ChatService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

// 사용자의 요청 정보에서 토큰을 꺼내서 토큰이 우리가 만들어준 토큰인지 아닌지를 검증
@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final ChatService chatService;

    @Value(("${jwt.secretKey}"))
    private String secretKey;

    // (connect, disconnect, subscribe)하기 전 메서드를 탐
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // 액세서 안에서 토큰을 이제 꺼내기
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);


        // 프론트 코드에서도 CONNECT 후 DISCONNECT, SUBSCRIBE 하기에 CONNECT만 검증
        if (StompCommand.CONNECT == accessor.getCommand()) {
            log.info("StompHandler preSend connect 시 토큰 유효성 검증");
            String bearerToken = accessor.getFirstNativeHeader("Authorization");
            String token = bearerToken.substring(7);

            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            log.info("StompHandler preSend connect 시 토큰 유효성 검증 완료 : {}", StompCommand.CONNECT == accessor.getCommand());
        }

//        if(StompCommand.SUBSCRIBE == accessor.getCommand()){
//            System.out.println("subscribe 검증");
//            String bearerToken = accessor.getFirstNativeHeader("Authorization");
//            String token = bearerToken.substring(7);
//            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(secretKey)
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//            String email = claims.getSubject();
//            String roomId = accessor.getDestination().split("/")[2];
//            if(!chatService.isRoomParticipant(email, Long.parseLong(roomId))){
//                throw new RuntimeException("해당 room에 권한이 없습니다.");
//            }
//        }
        return message;
    }
}

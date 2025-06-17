package chatserver.chat.config;

import chatserver.chat.handler.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/connect")
                .setAllowedOrigins("http://localhost:3000")
                //ws가 아닌 http 엔드포인트를 사용할 수 있게 해줌
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry
                // 메시지를 발행할 때는 /publish/1
                // /publish로 시작하는 url패턴으로 메시지가 발행 되면 @Controller가 붙어있는
                // 객체의 @MessageMapping 메서드로 라우팅
                .setApplicationDestinationPrefixes("/publish")
                // 메시지를 수신할 때 /topic/1
                .enableSimpleBroker("/topic");
    }

    /**
     * 웹소켓 요청 (connect, disconnect, subscribe)등의 요청시에는 http header등 http 메시지를 넣어올 수 있고
     * 이를 인터셉터가 가로채서 StompHandler.preSend() 토큰 등을 검증
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}

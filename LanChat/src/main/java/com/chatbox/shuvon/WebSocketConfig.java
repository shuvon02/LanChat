package com.chatbox.shuvon;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(new UserHandshakeHandler()) // ✅ IMPORTANT
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // group + private
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user"); // required for private messaging
    }

    /*@Transactional
    public void save(Entity  a, Entity b) {

        try{
            Session sessionCleint = hbm.openSession();
            Session sessionSuper = hbm.openSession();


            sessionCleint.persist(a);
            sessionSuper.persist(b);


        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(session != null){
                close session;
            }
        }

    }*/

}
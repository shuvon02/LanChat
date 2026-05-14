package com.chatbox.shuvon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        return message;
    }

    @MessageMapping("/join")
    @SendTo("/topic/users")
    public String join(String username) {
        return username;
    }

    // ✅ PRIVATE MESSAGE
    @MessageMapping("/private")
    public void sendPrivate(ChatMessage message) {
        messagingTemplate.convertAndSendToUser(
                message.getReceiver(),
                "/queue/messages",
                message
        );
    }
}
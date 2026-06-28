package com.chatbox.shuvon;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

public class UserHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {

        // get username from query param
        String username = request.getURI().getQuery();

        System.err.println("user name: " + username);
        System.err.println("user name: " + username);

        if (username != null && username.startsWith("username=")) {
            username = username.split("=")[1];
        } else {
            username = "user-" + UUID.randomUUID();
        }

        final String finalUsername = username;

        return () -> finalUsername;
    }
}

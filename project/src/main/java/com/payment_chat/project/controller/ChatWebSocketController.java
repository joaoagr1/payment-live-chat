package com.payment_chat.project.controller;

import com.payment_chat.project.dto.ChatMessageRequest;
import com.payment_chat.project.dto.MessageDTO;
import com.payment_chat.project.model.User;
import com.payment_chat.project.service.MessageService;
import com.payment_chat.project.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketController {

    private final MessageService messageService;
    private final UserService userService;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageRequest request,
                           SimpMessageHeaderAccessor headerAccessor) {
        Authentication authentication = (Authentication) headerAccessor.getUser();
        if (authentication != null) {
            User sender = userService.getCurrentUser(authentication);
            messageService.sendMessage(sender, request.getRecipientId(), request.getContent());
        }
    }

    @MessageMapping("/chat.connect")
    public void userConnect(SimpMessageHeaderAccessor headerAccessor) {
        Authentication authentication = (Authentication) headerAccessor.getUser();
        if (authentication != null) {
            User user = userService.getCurrentUser(authentication);
            userService.setUserOnlineStatus(user, true);
            log.info("User connected: {}", user.getUsername());
        }
    }

    @MessageMapping("/chat.disconnect")
    public void userDisconnect(SimpMessageHeaderAccessor headerAccessor) {
        Authentication authentication = (Authentication) headerAccessor.getUser();
        if (authentication != null) {
            User user = userService.getCurrentUser(authentication);
            userService.setUserOnlineStatus(user, false);
            log.info("User disconnected: {}", user.getUsername());
        }
    }
}

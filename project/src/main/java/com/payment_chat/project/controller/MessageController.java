package com.payment_chat.project.controller;

import com.payment_chat.project.dto.ChatMessageRequest;
import com.payment_chat.project.dto.MessageDTO;
import com.payment_chat.project.model.User;
import com.payment_chat.project.service.MessageService;
import com.payment_chat.project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<MessageDTO> sendMessage(
            @Valid @RequestBody ChatMessageRequest request,
            Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication);
        MessageDTO message = messageService.sendMessage(currentUser, request.getRecipientId(), request.getContent());
        return ResponseEntity.ok(message);
    }

    @GetMapping("/conversation/{userId}")
    public ResponseEntity<Page<MessageDTO>> getConversation(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication);
        Page<MessageDTO> messages = messageService.getConversation(currentUser, userId, page, size);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/{messageId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long messageId,
            Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication);
        messageService.markAsRead(messageId, currentUser);
        return ResponseEntity.noContent().build();
    }
}

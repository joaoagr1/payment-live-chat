package com.payment_chat.project.service;

import com.payment_chat.project.dto.MessageDTO;
import com.payment_chat.project.model.Message;
import com.payment_chat.project.model.Message.MessageType;
import com.payment_chat.project.model.User;
import com.payment_chat.project.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import static com.payment_chat.project.config.RabbitMQConfig.CHAT_EXCHANGE;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final RabbitTemplate rabbitTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public MessageDTO sendMessage(User sender, Long recipientId, String content) {
        User recipient = userService.findById(recipientId);

        if (!friendshipService.areFriends(sender, recipient)) {
            throw new RuntimeException("Can only send messages to friends");
        }

        Message message = Message.builder()
                .sender(sender)
                .recipient(recipient)
                .content(content)
                .type(MessageType.TEXT)
                .build();

        message = messageRepository.save(message);
        MessageDTO messageDTO = toDTO(message);

        // Send to RabbitMQ
        rabbitTemplate.convertAndSend(CHAT_EXCHANGE, "chat.message", messageDTO);

        // Send via WebSocket
        messagingTemplate.convertAndSendToUser(
                recipient.getUsername(),
                "/queue/messages",
                messageDTO
        );

        return messageDTO;
    }

    public Page<MessageDTO> getConversation(User currentUser, Long otherUserId, int page, int size) {
        User otherUser = userService.findById(otherUserId);

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("sentAt").descending());

        return messageRepository.findConversation(currentUser, otherUser, pageRequest)
                .map(this::toDTO);
    }

    @Transactional
    public void markAsRead(Long messageId, User currentUser) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!message.getRecipient().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Not authorized");
        }

        if (message.getReadAt() == null) {
            message.setReadAt(LocalDateTime.now());
            messageRepository.save(message);
        }
    }

    private MessageDTO toDTO(Message message) {
        return MessageDTO.builder()
                .id(message.getId())
                .senderId(message.getSender().getId())
                .senderUsername(message.getSender().getUsername())
                .recipientId(message.getRecipient().getId())
                .recipientUsername(message.getRecipient().getUsername())
                .content(message.getContent())
                .type(message.getType())
                .sentAt(message.getSentAt())
                .readAt(message.getReadAt())
                .build();
    }
}

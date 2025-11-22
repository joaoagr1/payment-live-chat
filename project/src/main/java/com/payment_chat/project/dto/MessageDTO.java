package com.payment_chat.project.dto;

import com.payment_chat.project.model.Message.MessageType;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MessageDTO {
    private Long id;
    private Long senderId;
    private String senderUsername;
    private Long recipientId;
    private String recipientUsername;
    private String content;
    private MessageType type;
    private PaymentDTO payment;
    private LocalDateTime sentAt;
    private LocalDateTime readAt;
}

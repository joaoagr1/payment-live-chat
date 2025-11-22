package com.payment_chat.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ChatMessageRequest {
    @NotNull(message = "Recipient ID is required")
    private Long recipientId;

    @NotBlank(message = "Content is required")
    private String content;
}

package com.payment_chat.project.dto;

import com.payment_chat.project.model.Payment.PaymentMethod;
import com.payment_chat.project.model.Payment.PaymentStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PaymentDTO {
    private Long id;
    private UserDTO requester;
    private UserDTO payer;
    private BigDecimal amount;
    private String description;
    private PaymentStatus status;
    private PaymentMethod method;
    private String pixQrCode;
    private String pixQrCodeBase64;
    private String pixCopyPaste;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime paidAt;
}

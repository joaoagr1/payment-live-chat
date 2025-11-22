package com.payment_chat.project.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.payment_chat.project.dto.PaymentDTO;
import com.payment_chat.project.model.Message;
import com.payment_chat.project.model.Message.MessageType;
import com.payment_chat.project.model.User;
import com.payment_chat.project.repository.MessageRepository;
import com.payment_chat.project.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${mercadopago.access-token}")
    private String mercadoPagoAccessToken;

    @Transactional
    public PaymentDTO createPaymentRequest(User requester, Long payerId, BigDecimal amount, String description) {
        User payer = userService.findById(payerId);

        if (!friendshipService.areFriends(requester, payer)) {
            throw new RuntimeException("Can only request payment from friends");
        }

        // Initialize Mercado Pago
        MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);

        try {
            // Create PIX payment
            PaymentClient paymentClient = new PaymentClient();

            PaymentCreateRequest paymentRequest = PaymentCreateRequest.builder()
                    .transactionAmount(amount)
                    .description(description)
                    .paymentMethodId("pix")
                    .payer(PaymentPayerRequest.builder()
                            .email(payer.getEmail())
                            .build())
                    .build();

            Payment mpPayment = paymentClient.create(paymentRequest);

            // Save payment
            com.payment_chat.project.model.Payment payment = com.payment_chat.project.model.Payment.builder()
                    .requester(requester)
                    .payer(payer)
                    .amount(amount)
                    .description(description)
                    .mercadoPagoPaymentId(mpPayment.getId().toString())
                    .pixQrCode(mpPayment.getPointOfInteraction().getTransactionData().getQrCode())
                    .pixQrCodeBase64(mpPayment.getPointOfInteraction().getTransactionData().getQrCodeBase64())
                    .pixCopyPaste(mpPayment.getPointOfInteraction().getTransactionData().getQrCode())
                    .expiresAt(convertToLocalDateTime(mpPayment.getDateOfExpiration()))
                    .build();

            payment = paymentRepository.save(payment);

            // Create message with payment
            Message message = Message.builder()
                    .sender(requester)
                    .recipient(payer)
                    .content("Payment request: " + description)
                    .type(MessageType.PAYMENT_REQUEST)
                    .payment(payment)
                    .build();

            messageRepository.save(message);

            PaymentDTO paymentDTO = toDTO(payment);

            // Notify via WebSocket
            messagingTemplate.convertAndSendToUser(
                    payer.getUsername(),
                    "/queue/payments",
                    paymentDTO
            );

            return paymentDTO;

        } catch (MPApiException | MPException e) {
            log.error("Error creating Mercado Pago payment", e);
            throw new RuntimeException("Failed to create payment: " + e.getMessage());
        }
    }

    @Transactional
    public void handlePaymentWebhook(String paymentId) {
        try {
            MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);
            PaymentClient paymentClient = new PaymentClient();
            Payment mpPayment = paymentClient.get(Long.parseLong(paymentId));

            com.payment_chat.project.model.Payment payment = paymentRepository
                    .findByMercadoPagoPaymentId(paymentId)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));

            // Update payment status
            payment.setStatus(mapMercadoPagoStatus(mpPayment.getStatus()));
            if ("approved".equals(mpPayment.getStatus())) {
                payment.setPaidAt(LocalDateTime.now());
            }
            paymentRepository.save(payment);

            // Notify users
            PaymentDTO paymentDTO = toDTO(payment);
            messagingTemplate.convertAndSendToUser(
                    payment.getPayer().getUsername(),
                    "/queue/payments",
                    paymentDTO
            );
            messagingTemplate.convertAndSendToUser(
                    payment.getRequester().getUsername(),
                    "/queue/payments",
                    paymentDTO
            );

        } catch (MPApiException | MPException e) {
            log.error("Error processing webhook", e);
        }
    }

    public List<PaymentDTO> getUserPayments(User user) {
        return paymentRepository.findByUser(user)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private com.payment_chat.project.model.Payment.PaymentStatus mapMercadoPagoStatus(String mpStatus) {
        return switch (mpStatus) {
            case "approved" -> com.payment_chat.project.model.Payment.PaymentStatus.APPROVED;
            case "rejected" -> com.payment_chat.project.model.Payment.PaymentStatus.REJECTED;
            case "cancelled" -> com.payment_chat.project.model.Payment.PaymentStatus.CANCELLED;
            default -> com.payment_chat.project.model.Payment.PaymentStatus.PENDING;
        };
    }

    private LocalDateTime convertToLocalDateTime(OffsetDateTime offsetDateTime) {
        return offsetDateTime != null ? offsetDateTime.toLocalDateTime() : null;
    }

    private PaymentDTO toDTO(com.payment_chat.project.model.Payment payment) {
        return PaymentDTO.builder()
                .id(payment.getId())
                .requester(userService.toDTO(payment.getRequester()))
                .payer(userService.toDTO(payment.getPayer()))
                .amount(payment.getAmount())
                .description(payment.getDescription())
                .status(payment.getStatus())
                .method(payment.getMethod())
                .pixQrCode(payment.getPixQrCode())
                .pixQrCodeBase64(payment.getPixQrCodeBase64())
                .pixCopyPaste(payment.getPixCopyPaste())
                .createdAt(payment.getCreatedAt())
                .expiresAt(payment.getExpiresAt())
                .paidAt(payment.getPaidAt())
                .build();
    }
}

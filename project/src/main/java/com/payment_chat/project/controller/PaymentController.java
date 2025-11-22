package com.payment_chat.project.controller;

import com.payment_chat.project.dto.PaymentDTO;
import com.payment_chat.project.dto.PaymentRequestDTO;
import com.payment_chat.project.model.User;
import com.payment_chat.project.service.PaymentService;
import com.payment_chat.project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<PaymentDTO> createPaymentRequest(
            @Valid @RequestBody PaymentRequestDTO request,
            Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication);
        PaymentDTO payment = paymentService.createPaymentRequest(
                currentUser,
                request.getPayerId(),
                request.getAmount(),
                request.getDescription()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getUserPayments(Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication);
        List<PaymentDTO> payments = paymentService.getUserPayments(currentUser);
        return ResponseEntity.ok(payments);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> mercadoPagoWebhook(@RequestBody Map<String, Object> payload) {
        log.info("Received Mercado Pago webhook: {}", payload);

        try {
            String type = (String) payload.get("type");
            if ("payment".equals(type)) {
                Map<String, Object> data = (Map<String, Object>) payload.get("data");
                String paymentId = data.get("id").toString();
                paymentService.handlePaymentWebhook(paymentId);
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error processing webhook", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
